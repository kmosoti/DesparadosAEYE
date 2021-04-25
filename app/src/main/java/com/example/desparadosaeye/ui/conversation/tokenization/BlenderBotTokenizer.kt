package com.example.desparadosaeye.ui.conversation.tokenization

import android.text.TextUtils
import java.util.*

private const val UNK_TOKEN: Int = 3
private const val UNK_STRING = "__unk__"
private const val spaceStrings = "([ '/\",.\\n<>!?])"

// copied from open source example:
// https://github.com/huggingface/tflite-android-transformers/blob/master/gpt2/src/main/java/co/huggingface/android_transformers/gpt2/tokenization/GPT2Tokenizer.kt
class BlenderBotTokenizer(
    private val encoder: Map<String, Int>,
    private val decoder: Map<Int, String>,
    private val bpeRanks: Map<Pair<String, String>, Int>
) {

    fun decode(tokens: List<Int>): String {

        // filter out any __start__, __end__, or __unk__ tokens
        // decode all tokens
        // sequentially add token strings to output string
        //  - if prev was a completing word and this token is at least a starting word,
        //      make sure to first insert a space in the output string
        //  - if prev was an ending punctuation mark, capitalize first char of this word
        //      if it is an alphabet letter
        //  - if decoded token is an incomplete word and the last word, do not add it
        //  - otherwise, trim off trailing "@@".
        //  - then add the decoded token to the sequence






        // TODO remove stuttering in tokens
        // TODO capitalize first char at begining and after certain punctuation marks
        return tokens.joinToString(" ") { decoder.getOrDefault(it, "") }
            .replace(UNK_STRING, "", true)
            //.replace("(.) $spaceStrings".toRegex(), {it.value[0]+it.value.substring(2)})
        //val utfCodepoints = text.map { byteDecoder[it.toString()]!! }
        //return String(utfCodepoints.toIntArray(), 0, utfCodepoints.size)
        //return text
    }

    fun encode(text: String): MutableList<Int> {

        // insert a space before punctuation marks (.,?!'")
        // also insert another space after quotation marks (")
        // split text by whitespace
        // trim and lowercase text pieces
        // for each text piece
        //  - look for an exact match
        //  - if an exact match doesn't exist,
        //          look for sequences of s@@ tokens (and a regular final s token)
        //          that will match. If a matching token sequence is found, add all
        //          tokens to the tokens list
        //  - if no exact or wordpiece match exists, add an UNK_TOKEN to the tokens list







        val spaceRegex = spaceStrings.toRegex()
        val spacedText = text.replace(spaceRegex) { " " + it.value + " " }
        print("spacedText: $spacedText")

        val strings: List<String> = TextUtils.split(spacedText, " ")
            .filter { it.trim().isNotEmpty() }
            .map { it.toLowerCase(Locale.ROOT) }

        print("these are the string: |$strings|\n")
        for (elem in strings) {
            print("elem i: |$elem|\n")
        }

        val tokens = strings.map { encoder[it] ?: UNK_TOKEN }

        print("these are the tokens: |$tokens|\n")
        for (elem in tokens) {
            print("elem i: $elem\n")
        }

        return tokens.toMutableList()
    }
}