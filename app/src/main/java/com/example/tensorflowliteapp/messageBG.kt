package com.example.tensorflowliteapp

import com.example.tensorflowliteapp.ml.EfficientdetLite2
import java.lang.Math.abs

class messageBG {
    val translate = mapOf(
        "person" to "човек",
        "bicycle" to "колело",
        "car" to "кола",
        "motorcycle" to "мотоциклет",
        "airplane" to "самолет",
        "bus" to "автобус",
        "train" to "влак",
        "truck" to "камион",
        "boat" to "лодка",
        "traffic light" to "светофар",
        "fire hydrant" to "пожарен кран",
        "stop sign" to "знак стоп",
        "parking meter" to "паркинг автомат",
        "bench" to "пейка",
        "bird" to "птица",
        "cat" to "котка",
        "dog" to "куче",
        "horse" to "кон",
        "sheep" to "офца",
        "cow" to "крава",
        "elephant" to "слон",
        "bear" to "мечка",
        "zebra" to "зебра",
        "giraffe" to "жираф",
        "backpack" to "раница",
        "umbrella" to "чадър",
        "handbag" to "дамска чанта",
        "tie" to "вратовръзка",
        "suitcase" to "куфар",
        "frisbee" to "фризби",
        "skis" to "ски",
        "snowboard" to "сноуборд",
        "sports ball" to "спортна топка",
        "kite" to "хвърчило",
        "baseball bat" to "бейзболна бухалка",
        "baseball glove" to "бейзболна ръкавица",
        "skateboard" to "скейтборд",
        "surfboard" to "дъска за сърф",
        "tennis racket" to "тенис ракета",
        "bottle" to "бутилка",
        "wine glass" to "чаша за вино",
        "cup" to "чаша",
        "fork" to "вилица",
        "knife" to "нож",
        "spoon" to "лъжица",
        "bowl" to "купа",
        "banana" to "банан",
        "apple" to "ябълка",
        "sandwich" to "сандвич",
        "orange" to "портокал",
        "broccoli" to "броколи",
        "carrot" to "морков",
        "hot dog" to "хот дог",
        "pizza" to "пица",
        "donut" to "донът",
        "cake" to "торта",
        "chair" to "стол",
        "couch" to "диван",
        "potted plant" to "растение в саксия",
        "bed" to "легло",
        "dining table" to "маса",
        "toilet" to "тоалетна",
        "tv" to "телевизор",
        "laptop" to "лаптоп",
        "mouse" to "мишка",
        "remote" to "дистанционно",
        "keyboard" to "клавиатура",
        "cell phone" to "телефон",
        "microwave" to "микроволнова",
        "oven" to "фурна",
        "toaster" to "тостер",
        "sink" to "мивка",
        "refrigerator" to "хладилник",
        "book" to "книга",
        "clock" to "часовник",
        "vase" to "ваза",
        "scissors" to "ножици",
        "teddy bear" to "плюшено мече",
        "hair drier" to "сешоар",
        "toothbrush" to "четка за зъби",
    )
    fun rod(word : String): Int {

        val words = listOf(word.split(" "))
        val lastChar = words[0][words[0].size-1]
        if(lastChar == "a" || lastChar == "я"){
            return 2
        }else if(lastChar == "o" || lastChar == "e"){
            return 3
        }else{
            return 1
        }
    }
    fun num(br : Int, word : String) : String? {
        val word = translate[word]
        if(br > 2){
            return br.toString()
        }else if(br == 1){
            if(word?.let { rod(it) } == 1){
                return "Един"
            }else if(word?.let { rod(it) } == 2) {
                return "Една"
            }else{
                return "Едно"
            }
        }else if(br == 2){
            if(word?.let { rod(it) } == 1){
                return "Два"
            }else if(word?.let { rod(it) } == 2) {
                return "Две"
            }else{
                return "Две"
            }
        }
        return br.toString() + " "
    }

    val plural = mapOf(
        "person" to "човека",
        "bicycle" to "колела",
        "car" to "коли",
        "motorcycle" to "мотоциклета",
        "airplane" to "самолета",
        "bus" to "автобуса",
        "train" to "влака",
        "truck" to "камиона",
        "boat" to "лодки",
        "traffic light" to "светофара",
        "fire hydrant" to "пожарни крана",
        "stop sign" to "знака стоп",
        "parking meter" to "паркинг автомата",
        "bench" to "пейки",
        "bird" to "птици",
        "cat" to "котки",
        "dog" to "кучета",
        "horse" to "коня",
        "sheep" to "офце",
        "cow" to "крави",
        "elephant" to "слона",
        "bear" to "мечки",
        "zebra" to "зебри",
        "giraffe" to "жирафа",
        "backpack" to "раници",
        "umbrella" to "чадъра",
        "handbag" to "дамска чанти",
        "tie" to "вратовръзки",
        "suitcase" to "куфара",
        "frisbee" to "фризбита",
        "skis" to "ски",
        "snowboard" to "сноуборда",
        "sports ball" to "спортни топки",
        "kite" to "хвърчила",
        "baseball bat" to "бейзболни бухалки",
        "baseball glove" to "бейзболни ръкавици",
        "skateboard" to "скейтборда",
        "surfboard" to "дъски за сърф",
        "tennis racket" to "тенис ракети",
        "bottle" to "бутилки",
        "wine glass" to "чаши за вино",
        "cup" to "чаши",
        "fork" to "вилици",
        "knife" to "ножа",
        "spoon" to "лъжици",
        "bowl" to "купи",
        "banana" to "банана",
        "apple" to "ябълки",
        "sandwich" to "сандвича",
        "orange" to "портокала",
        "broccoli" to "броколи",
        "carrot" to "моркови",
        "hot dog" to "хот дога",
        "pizza" to "пици",
        "donut" to "донъта",
        "cake" to "торти",
        "chair" to "стола",
        "couch" to "дивана",
        "potted plant" to "растения в саксия",
        "bed" to "легла",
        "dining table" to "маси",
        "toilet" to "тоалетни",
        "tv" to "телевизора",
        "laptop" to "лаптопа",
        "mouse" to "мишки",
        "remote" to "дистанционни",
        "keyboard" to "клавиатури",
        "cell phone" to "телефона",
        "microwave" to "микроволнови",
        "oven" to "фурни",
        "toaster" to "тостера",
        "sink" to "мивки",
        "refrigerator" to "хладилника",
        "book" to "книги",
        "clock" to "часовника",
        "vase" to "вази",
        "scissors" to "ножици",
        "teddy bear" to "плюшени мечета",
        "hair drier" to "сешоара",
        "toothbrush" to "четки за зъби"
    )
    fun messageBg(arr : EfficientdetLite2.Outputs, word: String): String {
        var result = "Има "
        var objects = listOf<String>()
        if(word == "all"){
            if(arr.detectionResultList.size == 0) {
                return "Няма намерени обекти"
            }
            result += arr.detectionResultList.size.toString() + " обекта намерни."
            arr.detectionResultList.forEachIndexed { index, detectionResult ->
                 if (!objects.contains(detectionResult.categoryAsString)){

                 }
                 objects += detectionResult.categoryAsString
                 var brL = count(arr,detectionResult.categoryAsString,"left")
                 var brM = count(arr,detectionResult.categoryAsString,"left")
                 var brR = count(arr,detectionResult.categoryAsString,"left")
                 if(brL > 0){
                     result += num(brL,detectionResult.categoryAsString).toString() + " "
                     if(brL > 1){
                         result += plural[detectionResult.categoryAsString]
                     }else{
                         result += translate[detectionResult.categoryAsString]
                     }
                     result += " вляво, "
                 }
                 if(brR > 0){
                     result += num(brR,detectionResult.categoryAsString).toString() + " "
                     if(brR > 1){
                         result += plural[detectionResult.categoryAsString]
                     }else{
                         result += translate[detectionResult.categoryAsString]
                     }
                     result += " вдясно, "
                 }
            }

        }else{
            if(arr.detectionResultList.size == 0){
                if(word == "person"){
                    return "Няма намерени хора"
                }
                return "Няма намерени " + plural[word]
            }
            var br = count(arr,arr.detectionResultList[0].categoryAsString)
            val distanceCalulator = distanceCalulator()
            if(br > 1){
                result += br.toString() + " " +plural[arr.detectionResultList[0].categoryAsString] + " пред телефона."

                arr.detectionResultList.forEachIndexed { index, detectionResult ->
                    result += num(1,detectionResult.categoryAsString).toString() + " " +distanceCalulator.distance(detectionResult.categoryAsString, size(detectionResult),"bg") + " и е "
                    result += pos(detectionResult)
                }
            }else{
                result += num(1,arr.detectionResultList[0].categoryAsString) + " " + translate[arr.detectionResultList[0].categoryAsString] + " намерен " + distanceCalulator.distance(arr.detectionResultList[0].categoryAsString,size(arr.detectionResultList[0]), "bg") + " и "
                result += pos(arr.detectionResultList[0])
            }

        }
        return result
    }
    fun size(detectionResult : EfficientdetLite2.DetectionResult): Float {
        return abs(detectionResult.locationAsRectF.left - detectionResult.locationAsRectF.right) * abs(detectionResult.locationAsRectF.top - detectionResult.locationAsRectF.bottom)
    }
    fun count(arr : EfficientdetLite2.Outputs, word: String, loc: String = "all"): Int {
        var br = 0
        var gridCell : Array<Int?>
        var postProcessing = PostProcessing()
        arr.detectionResultList.forEachIndexed { index, detectionResult ->
            gridCell = postProcessing.gridPostion(detectionResult)
            if (detectionResult.categoryAsString == word) {
                if(loc == "left" && gridCell[0]!! <= 1){
                    br += 1
                }else if(loc == "mid" && gridCell[0] == 2){
                    br += 1
                }else if(loc == "right" && gridCell[0]!! >= 3){
                    br += 1
                }else if(loc == "all"){
                    br += 1
                }
            }
        }


        return br
    }

    fun pos(detectionResult: EfficientdetLite2.DetectionResult): String {
        var postProcessing = PostProcessing()
        var gridCell : Array<Int?>
        gridCell = postProcessing.gridPostion(detectionResult)
        var result = ""
        if(gridCell[0] == 0){
            result += "вляво"
        }
        if(gridCell[0] == 1){
            result += "леко вляво"
        }
        if(gridCell[0] == 2){
            result += "по средата"
        }
        if(gridCell[0] == 3){
            result += "леко вдясно"
        }
        if(gridCell[0] == 4){
            result += "вдясно"
        }

        if(gridCell[1] == 0){
            result += " нагоре."
        }
        if(gridCell[1] == 1){
            result += " и леко нагоре. "
        }
        if(gridCell[1] == 2){
            result += ". "
        }
        if(gridCell[1] == 3){
            result += " и леко надолу. "
        }
        if(gridCell[1] == 4){
            result += " и долу. "
        }
        return result
    }
}
/*

def messageBG(arr, word):
    result = "Има "
    objects = []
    if word == "all":
        if len(arr) == 0:
            return "Няма намерени обекти"
        result += str(len(arr)) + " обекта намерени. "
        for det in arr:
            if det[0] in objects:
                continue
            objects.append(det[0])
            brL = count(arr, det[0], "left")
            brM = count(arr, det[0], "mid")
            brR = count(arr, det[0], "right")
            if brL>0:
                result += str(num(brL,det[0])) + " "
                if brL>1:
                    result += plural[det[0]]
                else:
                    result += translate[det[0]]
                result += " вляво, "
            if brM>0:
                result += str(num(brM,det[0])) + " "
                if brM>1:
                    result += plural[det[0]]
                else:
                    result += translate[det[0]]
                result += " по средата, "
            if brR>0:
                result += str(num(brR,det[0])) + " "
                if brR>1:
                    result += plural[det[0]]
                else:
                    result += translate[det[0]]
                result += " вдясно, "
    else:
        if len(arr) == 0:
            if word == "person":
                return "Няма намерени хора"
            return "Няма намерени " + plural[word]
        br = count(arr, arr[0][0])
        if br>1:
            result += str(br) + " " + plural[arr[0][0]] + " пред телефона. "
            for det in arr:
                result += str(num(1,det[0]))+" е " + distance(det[0], det[3], "bg") + " и е "
                result += pos(det)
        else:
            result += str(num(1,arr[0][0])) + " " + translate[arr[0][0]] + " намерен " + distance(arr[0][0], arr[0][3], "bg") + " и "
            result += pos(arr[0])

    return result

def count(arr, word, loc="all"):
    br = 0
    for det in arr:
        if det[0] == word:
            if loc == "left" and det[1]<=1:
                br+=1
            elif loc == "mid" and det[1]==2:
                br += 1
            elif loc == "right" and det[1]>=3:
                br += 1
            elif loc == "all":
                br+=1

    return br

def pos(det):
    result = ""
    if det[1] == 0:
        result += "вляво"
    if det[1] == 1:
        result += "леко вляво"
    if det[1] == 2:
        result += "по средата"
    if det[1] == 3:
        result += "леко вдясно"
    if det[1] == 4:
        result += "вдясно"

    if det[2] == 0:
        result += " и горе. "
    if det[2] == 1:
        result += " и леко нагоре. "
    if det[2] == 2:
        result += ". "
    if det[2] == 3:
        result += " и леко надолу. "
    if det[2] == 4:
        result += " и долу. "
    return result
*/