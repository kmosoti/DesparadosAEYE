package com.example.desparadosaeye.ui.conversation.tokenization

import android.text.TextUtils

private const val UNK_TOKEN: Int = 3

// copied from open source example:
// https://github.com/huggingface/tflite-android-transformers/blob/master/gpt2/src/main/java/co/huggingface/android_transformers/gpt2/tokenization/GPT2Tokenizer.kt
class BlenderBotTokenizer(
    private val encoder: Map<String, Int>,
    private val decoder: Map<Int, String>,
    private val bpeRanks: Map<Pair<String, String>, Int>
) {
    private val encodeRegex = Regex("""'s|'t|'re|'ve|'m|'ll|'d| ?\p{L}+| ?\p{N}+| ?[^\s\p{L}\p{N}]+|\s+(?!\S)|\s+""")

    fun decode(tokens: List<Int>): String {
        val text = tokens.joinToString("") { decoder.getOrDefault(it, "") }
        val utfCodepoints = text.map { byteDecoder[it.toString()]!! }
        return String(utfCodepoints.toIntArray(), 0, utfCodepoints.size)
    }

    fun encode(text: String): MutableList<Int> {
        val tokens = encodeRegex.findAll(text).map { result ->
            result.value.codePoints()
                .boxed()
                .map { byteEncoder[it]!! }
                .toArray()
                .joinToString("")
        }
        /*val strings: Array<String> = TextUtils.split(text, splitRegex)

        print("these are the string: |$strings|\n")
        for (elem in strings) {
            print("elem i: $elem\n")
        }

        val tokens = strings
            .filter { it.length > 0 }
            .map { encoder[it] ?: UNK_TOKEN }

        print("these are the tokens: |$tokens|\n")
        for (elem in tokens) {
            print("elem i: $elem\n")
        }

        return tokens.toMutableList()*/


        val finalTokens = tokens
            .map { bpe(it) }
            .flatten()
            .map { encoder[it] ?: UNK_TOKEN }

        print("these are the final tokens: |$finalTokens|\n")
        for (elem in finalTokens) {
            print("elem i: $elem\n")
        }

        return finalTokens.toMutableList()
    }

    private fun bpe(token: String): List<String> {
        if (token.length <= 1) return listOf(token)

        var word = token.map { it.toString() }
        var pairs = getPairs(word)

        while (true) {
            if (!pairs.any { bpeRanks.containsKey(it) }) break
            val (first, second) = pairs.minBy { bpeRanks.getOrDefault(it, Int.MAX_VALUE) } ?: break

            var i = 0
            val newWord = mutableListOf<String>()
            while (i < word.size) {
                val j = word.withIndex().indexOfFirst { it.index >= i && it.value == first }
                if (j != -1) {
                    newWord.addAll(word.subList(i, j))
                    i = j
                } else {
                    newWord.addAll(word.subList(i, word.size))
                    break
                }

                if (word[i] == first && i < word.size-1 && word[i + 1] == second) {
                    newWord.add(first + second)
                    i += 2
                } else {
                    newWord.add(word[i])
                    i += 1
                }
            }

            word = newWord
            if (word.size == 1) {
                break
            } else {
                pairs = getPairs(word)
            }
        }

        return word
    }

    private fun getPairs(word: List<String>): Set<Pair<String, String>> {
        return mutableSetOf<Pair<String, String>>().apply {
            for (i in 0 until word.size-1) {
                add(word[i] to word[i + 1])
            }
        }
    }
}