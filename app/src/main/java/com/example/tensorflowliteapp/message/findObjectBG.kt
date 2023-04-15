package com.example.tensorflowliteapp

import com.example.tensorflowliteapp.message.*

fun findObjectBG(outputs: MutableList<Result>, word: String, translator: Translator): String{

    var result = "Има "
    var counter = count(outputs,word)
    if(counter == 0){
        if(word == "person"){
            return "Няма намерени хора"
        }
        return "Няма намерени " + translator.pluralBG[word]
    }
    val distanceCalulator = distanceCalulator()
        result += translator.number(counter,word,"bg") + " " + oneOrManyBG(word,counter,translator) + " пред телефона."
        outputs.forEach { detectionResult ->
            if(detectionResult.category.equals(word)) {
                result += translator.number(1, detectionResult.category, "bg").toString() +
                        " " + distanceCalulator.distance(detectionResult.category, size(detectionResult),"bg") + " и е "
                result += possitionBG(detectionResult)
            }
        }

    return result
}