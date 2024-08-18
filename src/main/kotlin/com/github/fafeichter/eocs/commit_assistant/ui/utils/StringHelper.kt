package com.github.fafeichter.eocs.commit_assistant.ui.utils

class StringHelper {
    companion object {
        fun splitAndTrim(commaSeparatedString: String): Array<String> {
            return commaSeparatedString.split(",")
                .map { element -> element.trim() }
                .toTypedArray()
        }
    }
}