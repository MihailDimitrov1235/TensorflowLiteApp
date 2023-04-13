package com.example.tensorflowliteapp

import com.example.tensorflowliteapp.message.*

fun findObjectBG(outputs: MutableList<Result>, word: String, translator: Translator): String{

    log(word)
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
            if(detectionResult.getCategory().equals(word)) {
                result += translator.number(1, detectionResult.getCategory(), "bg").toString() +
                        " " + distanceCalulator.distance(detectionResult.getCategory(), size(detectionResult),"bg") + " и е "
                result += possitionBG(detectionResult)
            }
        }

    return result
}