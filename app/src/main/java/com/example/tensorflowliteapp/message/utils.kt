package com.example.tensorflowliteapp.message

import com.example.tensorflowliteapp.ml.EfficientdetLite2

fun size(detectionResult : EfficientdetLite2.DetectionResult): Float {
    return Math.abs(detectionResult.locationAsRectF.left - detectionResult.locationAsRectF.right) * Math.abs(
        detectionResult.locationAsRectF.top - detectionResult.locationAsRectF.bottom
    )
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

fun possitionBG(detectionResult: EfficientdetLite2.DetectionResult): String {
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