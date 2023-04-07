package com.example.tensorflowliteapp

    class Command(val templates: Array<String>) {
        fun match(text: String): Boolean {
            for (template in templates) {
                if (text.contains(template)) {
                    return true
                }
            }
            return false
        }
    }