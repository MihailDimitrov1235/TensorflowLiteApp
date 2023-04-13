package com.example.tensorflowliteapp.message

import com.example.tensorflowliteapp.Result

val imageWidth = 320
val imageHeight = 320

fun size(detectionResult: Result): Float {
    return Math.abs(detectionResult.getLocation().left - detectionResult.getLocation().right) * Math.abs(
        detectionResult.getLocation().top - detectionResult.getLocation().bottom
    )
}

fun oneOrManyBG(word: String, counter: Int, translator: Translator): String? {
    if (counter > 1){
        return translator.pluralBG[word]
    }
    return translator.translate(word,"bg")
}

fun count(arr: MutableList<Result>, word: String, loc: String = "all"): Int {
    var counter = 0
    var gridCell : Array<Int?>
    arr.forEach { detectionResult ->
            gridCell = gridPostion(detectionResult)
            if (detectionResult.getCategory() == word || word == "all") {
                if (loc == "left" && gridCell[0]!! <= 1) {
                    counter += 1
                } else if (loc == "mid" && gridCell[0] == 2) {
                    counter += 1
                } else if (loc == "right" && gridCell[0]!! >= 3) {
                    counter += 1
                } else if (loc == "all") {
                    counter += 1
                }
        }
    }
    return counter
}

fun possitionBG(detectionResult: Result): String {
    var gridCell : Array<Int?>
    gridCell = gridPostion(detectionResult)
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

fun gridPostion(
    detectionResult: Result
) : Array<Int?> {

    val location = detectionResult.getLocation()
    val centerObjectX = (location.left+location.right)/2
    val centerObjectY = (location.top+location.bottom)/2
    val gridCell = arrayOfNulls<Int>(2)


    if(centerObjectX<=imageWidth/5){
        gridCell[0] = 0
        //   textView.text = "Po X e mejdu 0 i 60;\ncategory="+category+";\nX="+x+"\nY="+y+"\nZ="+z
    }else if(centerObjectX>imageWidth/5 && centerObjectX<=imageWidth/5*2){
        gridCell[0] = 1
    }else if(centerObjectX>imageWidth/5*2 && centerObjectX<=imageWidth/5*3){
        gridCell[0] = 2
    }else if(centerObjectX>imageWidth/5*3 && centerObjectX<=imageWidth/5*4){
        gridCell[0] = 3
    }else if(centerObjectX>imageWidth/5*4){
        gridCell[0] = 4
    }
    if(centerObjectY<=imageHeight/5){
        //  textView.text = "Po Y e mejdu 0 i 60;\ncategory="+category
        gridCell[1] = 0
    }else if(centerObjectY>imageHeight/5 && centerObjectY<=imageHeight/5*2){
        gridCell[1] = 1
    }else if(centerObjectY>imageHeight/5*2 && centerObjectY<=imageHeight/5*3){
        gridCell[1] = 2
    }else if(centerObjectY>imageHeight/5*3 && centerObjectY<=imageHeight/5*4){
        gridCell[1] = 3
    }else if(centerObjectY>imageHeight/5*4){
        gridCell[1] = 4
    }
    return gridCell
}