package com.example.desparadosaeye.ui.conversation.tokenization

import android.text.TextUtils
import java.util.*

private const val NULL_TOKEN: Int = 0
private const val NULL_STRING = "__null__"
private const val START_TOKEN: Int = 1
const val START_STRING = "__start__"
private const val END_TOKEN: Int = 2
const val END_STRING = "__end__"
private const val UNK_TOKEN: Int = 3
private const val UNK_STRING = "__unk__"
private const val PERIOD_TOKEN = 5
private const val COMMA_TOKEN = 6
private val PUNCTUATION = arrayOf(".", ",", "!", "?")
private val SPECIAL_TOKENS = arrayOf(-1,
    NULL_TOKEN, START_TOKEN, END_TOKEN,
    UNK_TOKEN, PERIOD_TOKEN, COMMA_TOKEN)

class BlenderBotTokenizer(
    private val encoder: Map<String, Int>,
    private val decoder: Map<Int, String>,
    private val merges: Map<String, List<String>>
) {

    private val partialTokens = encoder
        .filter { kv -> kv.key.endsWith("@@") }
        .map { kv -> kv.key }

    fun decode(tokens: List<Int>): String {

        // remove repeated tokens (stuttering)
        val stutterStriped = mutableListOf<Int>()
        var prevToken = -1
        tokens
            // filter out any __start__, __end__, or __unk__ tokens
            .filter { it !in SPECIAL_TOKENS }
            .forEach {
                if (it != prevToken) {
                    prevToken = it
                    stutterStriped.add(it)
                }
            }

        //return "Tokens are: $tokens"
        val subwords = stutterStriped
        // decode all tokens
            .map { decoder[it] ?: "" }
            .toMutableList()

        print("\nSubwords: <$subwords>\n")

        // safety return
        if (subwords.size == 0) { return "" }
        // remove the last token if it is a partial word
        if (subwords.last().endsWith("@@")) {
            subwords.removeLast()
        }
        var thisWasEOS: Boolean
        var prevWasEOS = true
        var prevWasPartial = false

        return subwords.map {
            thisWasEOS = false
            val lowerStr = when {
                it.endsWith("@@") -> {
                    prevWasPartial = false
                    it.substring(0, it.length - 2)
                }
                it in PUNCTUATION -> {
                    prevWasPartial = false
                    thisWasEOS = true
                    it
                }
                else -> {
                    if (prevWasPartial) {
                        prevWasPartial = false
                        it
                    } else {
                        " $it"
                    }
                }
            }
            val finalStr = if(prevWasEOS) lowerStr.capitalize(Locale.ROOT) else lowerStr
            prevWasEOS = thisWasEOS
            finalStr
        }.joinToString("").trim()
        // if prev was a completing word and this token is at least a starting word,
        //  make sure to first insert a space in the output string
        // if prev was an ending punctuation mark, capitalize first char of this word
        //  if it is an alphabet letter
        //  - then add the decoded token to the sequence
    }

    private fun splitByMerges(it: String): List<String> {
        return when {
            encoder.containsKey(it) -> listOf(it)
            merges.containsKey(it) -> merges[it]!!
            else -> listOf(it)
        }
    }

    private fun greedyPartialSearch(trailingString: String): MutableList<String> {
        // base case
        if (trailingString.isEmpty()) { return mutableListOf() }

        //  - if an exact match doesn't exist,
        //          look for sequences of s@@ tokens (and a regular final s token)
        //          that will match. If a matching token sequence is found, add all
        //          tokens to the tokens list

        var currentMatch = mutableListOf<String>()
        var longestMatch = mutableListOf<String>()
        encoder.keys.forEach { subtoken ->
            // identify if subtoken is a partial subtoken
            if (subtoken.endsWith("@@")) {
                if (trailingString.startsWith(subtoken.substring(0, subtoken.length-2))) {
                    currentMatch = greedyPartialSearch(trailingString.substring(subtoken.length-2))
                    currentMatch.add(0, subtoken)
                }
            }
            else {
                if (trailingString == subtoken) {
                    currentMatch = mutableListOf(subtoken)
                }
                else {
                    currentMatch = mutableListOf()
                }
            }

            // only select if it makes longest match
            if (currentMatch.size > longestMatch.size) {
                longestMatch = currentMatch
            }
        }

        //  - if no exact or wordpiece match exists, add an UNK_TOKEN to the tokens list
        if (longestMatch.size == 0) {
            longestMatch.add(UNK_STRING)
        }
        return longestMatch
    }

    fun encode(text: String): MutableList<Int> {

        val wordPieces = text
            // insert a space before punctuation marks (.,?!'")
            .replace("([.,!?'\"])".toRegex(), { " " + it.value })
            // also insert another space after apostraphes (')
            .replace("\'", "\' ")
            // . . . and quotation marks (")
            .replace("\"", "\" ")
            // split text by whitespace
            .split("([ \n])".toRegex())
            // trim and lowercase text pieces
            .map { it.trim().toLowerCase(Locale.ROOT) }
            // do this 4 times to shake out any replacements
            //   for each text peice, if not directly in dictionary.
            .flatMap { splitByMerges(it) }
            .flatMap { splitByMerges(it) }
            .flatMap { splitByMerges(it) }
            .flatMap { splitByMerges(it) }

        print("\npre-split words $wordPieces\n")

        val tokens = mutableListOf<Int>().apply { wordPieces.forEach {
            //  - look for an exact match
            val directId = encoder[it]
            directId?.also { _ ->
                add(directId)
            } ?: run {
                //  - if an exact match doesn't exist,
                //          look for sequences of s@@ tokens (and a regular final s token)
                //          that will match. If a matching token sequence is found, add all
                //          tokens to the tokens list
                val matchingSeq = greedyPartialSearch(it)

                matchingSeq.forEach { substr ->
                    add(encoder[substr] ?: UNK_TOKEN)
                }
            }
        } }

        print("\n\nToken values: $tokens\n")

        return tokens
    }
}