package com.example.desparadosaeye.utils

import java.util.regex.Pattern

class ContentFilter {

    private val banned: Array<String> = arrayOf("xyz", "abc") // TODO

    fun filter(input: String): String {
        // filter language content from both human or AI
        var modified = input
        banned.forEach {
            // https://stackoverflow.com/questions/33254492/how-to-censored-bad-words-offensive-words-in-android-studio/33254897
            val rx: Pattern = Pattern.compile("\\b$it\\b", Pattern.CASE_INSENSITIVE)
            modified = rx.matcher(modified)
                .replaceAll(String(CharArray(it.length))
                    .replace('\u0000', '\u0000'))
        }
        return modified
    }
}