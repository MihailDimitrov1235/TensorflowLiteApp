package com.example.tensorflowliteapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import com.example.tensorflowliteapp.message.imageHeight
import com.example.tensorflowliteapp.message.imageWidth
import com.example.tensorflowliteapp.ml.EfficientdetLite0
import com.example.tensorflowliteapp.ml.EfficientdetLite1
import com.example.tensorflowliteapp.ml.EfficientdetLite2
import com.example.tensorflowliteapp.ml.EfficientdetLite4
import com.example.tensorflowliteapp.ml.Mobilenetv1
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class ObjectDetector {
    var imageProcessor: ImageProcessor
    var labels:List<String>
    var model0: EfficientdetLite0
    var model1: EfficientdetLite1
    var model2: EfficientdetLite2
    var model4: EfficientdetLite4
    var model: Mobilenetv1
    var threshold = 0.5

    constructor(context: Context){
        labels = FileUtil.loadLabels(context,"labels.txt")
        model0 = EfficientdetLite0.newInstance(context)
        model1 = EfficientdetLite1.newInstance(context)
        model2 = EfficientdetLite2.newInstance(context)
        model4 = EfficientdetLite4.newInstance(context)
        model = Mobilenetv1.newInstance(context)
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(imageHeight, imageWidth, ResizeOp.ResizeMethod.BILINEAR)).build()
    }

    fun detect(bitmap: Bitmap): MutableList<Result> {
        var image = TensorImage.fromBitmap(bitmap)
        image = imageProcessor.process(image)
        val outputs = model4.process(image)
        val outputList = mutableListOf<Result>()
        outputs.detectionResultList.forEach{detectionResult ->
            if (detectionResult.scoreAsFloat >= threshold){
                val result = Result(detectionResult.locationAsRectF,detectionResult.categoryAsString,detectionResult.scoreAsFloat)
                outputList.add(result)
            }
        }
        return outputList
    }

    fun destroy(){
        model0.close()
        model1.close()
        model2.close()
        model4.close()
        model.close()
    }
}

class Result(location: RectF, category: String, score: Float) {
    val location = location
    val category = category
    val score = score
}