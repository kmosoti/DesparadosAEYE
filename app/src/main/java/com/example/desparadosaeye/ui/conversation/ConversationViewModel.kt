package com.example.desparadosaeye.ui.conversation

import android.app.Application
import android.util.JsonReader
import androidx.lifecycle.*
import com.example.desparadosaeye.ui.conversation.tokenization.BlenderBotTokenizer
import kotlinx.coroutines.*
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.channels.FileChannel

import androidx.lifecycle.ViewModel
import com.example.desparadosaeye.data.ApplicationModel
import com.example.desparadosaeye.data.StatementOrigin
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

// code adopted from
// https://github.com/huggingface/tflite-android-transformers/blob/master/gpt2/src/main/java/co/huggingface/android_transformers/gpt2/ml/GPT2Client.kt
private const val SEQUENCE_LENGTH  = 32
private const val NUM_LITE_THREADS = 4
private const val MODEL_PATH       = "blenderbot.tflite"
private const val VOCAB_PATH       = "blenderbot-vocab.json"
private const val MERGES_PATH      = "blenderbot-merges.txt"
private const val OUTPUT_TFLITE_NODE_INDEX = 3112

class ConversationViewModel(application: Application) : AndroidViewModel(application) {
    private val initJob: Job

    private lateinit var tokenizer: BlenderBotTokenizer
    private lateinit var tflite: Interpreter

    private var START_TOKEN: Int = -1
    private var END_TOKEN: Int = -1

    val applicationModel = ApplicationModel()
    private var statementAdapter = StatementAdapter(applicationModel)
    private lateinit var _conversationFragment: ConversationFragment
    var conversationFragment: ConversationFragment
        get() = _conversationFragment
        set(value) {
            _conversationFragment = value
            conversationFragment.statementsRecyclerView.adapter = statementAdapter
        }

    init {
        print("Starting conversation view model init\n")
        applicationModel.conversationViewModel = this
        initJob = viewModelScope.launch {
            val encoder  = loadEncoder()
            val decoder  = encoder.entries.associateBy({ it.value }, { it.key })
            val bpeRanks = loadBpeRanks()

            tokenizer = BlenderBotTokenizer(encoder, decoder, bpeRanks)
            tflite    = loadModel()

            START_TOKEN = tokenizer.encode("__start__")[0]
            END_TOKEN = tokenizer.encode("__end__")[0]
        }
    }

    fun genorateResponse(userStatement: String): String {
        val userTokens = tokenizer.encode(userStatement)

        val userTokensBuffer = IntBuffer.allocate(SEQUENCE_LENGTH)
        val modelTokensBuffer = IntBuffer.allocate(SEQUENCE_LENGTH)

        val user_len: Int = userTokens.size
        var model_len: Int = 1

        // fill arrays with user content and __start__ token for AI
        // also right pad with 0's
        for (i in 0 until SEQUENCE_LENGTH) {
            userTokensBuffer.put(i, when {
                i<user_len -> userTokens[i]
                else -> END_TOKEN
            })
            modelTokensBuffer.put(i, when {
                i == 0 -> START_TOKEN
                else -> END_TOKEN
            })
        }

        val user_len_buffer = IntBuffer.allocate(1)
        val model_len_buffer = IntBuffer.allocate(1)
        user_len_buffer.put(0, user_len)
        // we update model_len_buffer on every loop iteration
        // so it is not initialized here

        val interpreterInputs = arrayOf(userTokensBuffer, modelTokensBuffer,
                                        user_len_buffer, model_len_buffer)
        val tokenOutputBuffer = IntBuffer.allocate(1)

        val outputMap = HashMap<Int, Any>()
        outputMap[OUTPUT_TFLITE_NODE_INDEX] = tokenOutputBuffer

        val modelTokens = mutableListOf<Int>()

        repeat(SEQUENCE_LENGTH) genloop@{

            model_len_buffer.put(0, model_len)
            tflite.runForMultipleInputsOutputs(interpreterInputs, outputMap)

            if (tokenOutputBuffer[0] == END_TOKEN) { // this means __end__
                return@genloop
            }

            // append token to model token seq
            model_len += 1
            modelTokens.add(tokenOutputBuffer[0])
        }

        // convert modelTokens to string
        return tokenizer.decode(modelTokens)
    }

    override fun onCleared() {
        super.onCleared()
        tflite.close()
    }

    private suspend fun loadModel(): Interpreter = withContext(Dispatchers.IO) {
        val assetFileDescriptor = getApplication<Application>().assets.openFd(MODEL_PATH)
        assetFileDescriptor.use {
            print("\n========= A-1\n==========\n")
            val fileChannel = FileInputStream(assetFileDescriptor.fileDescriptor).channel
            print("\n========A0=======\n")
            val modelBuffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                it.startOffset,
                it.declaredLength
            )

            print("\n=======A1========\n")
            val opts = Interpreter.Options()
            print("\n=======A2======\n")
            opts.setNumThreads(NUM_LITE_THREADS)
            print("\n=======A3========\n")
            return@use Interpreter(modelBuffer, opts)
        }
    }

    private suspend fun loadEncoder(): Map<String, Int> = withContext(Dispatchers.IO) {
        hashMapOf<String, Int>().apply {
            val vocabStream = getApplication<Application>().assets.open(VOCAB_PATH)
            vocabStream.use {
                val vocabReader = JsonReader(InputStreamReader(it, "UTF-8"))
                vocabReader.beginObject()
                while (vocabReader.hasNext()) {
                    val key = vocabReader.nextName()
                    val value = vocabReader.nextInt()
                    put(key, value)
                }
                vocabReader.close()
            }
        }
    }

    private suspend fun loadBpeRanks():Map<Pair<String, String>, Int> = withContext(Dispatchers.IO) {
        hashMapOf<Pair<String, String>, Int>().apply {
            val mergesStream = getApplication<Application>().assets.open(MERGES_PATH)
            mergesStream.use { stream ->
                val mergesReader = BufferedReader(InputStreamReader(stream))
                mergesReader.useLines { seq ->
                    seq.drop(1).forEachIndexed { i, s ->
                        val list = s.split(" ")
                        val keyTuple = list[0] to list[1]
                        put(keyTuple, i)
                    }
                }
            }
        }
    }

    fun respondToUserInput(input: String) {
        // add user's statement
        applicationModel.addStatement(StatementOrigin.USER, input)

        // get AI's response
        val output =  genorateResponse(applicationModel.statements.last().content)

        // add AI's response
        applicationModel.addStatement(StatementOrigin.AI, output)
    }

    fun notifyStatementRemoved(index: Int) {
        // remove from recyclerView
        statementAdapter.notifyItemRemoved(index)
    }

    fun notifyStatementAdded(index: Int) {
        // insert at end of recyclerView
        statementAdapter.notifyItemInserted(index)
        // scroll down to position of inserted item
        conversationFragment.statementsRecyclerView.scrollToPosition(index)
    }
}
