package com.example.tensorflowliteapp

import android.content.Context
import android.graphics.Bitmap
import com.example.tensorflowliteapp.ml.EfficientdetLite0
import com.example.tensorflowliteapp.ml.EfficientdetLite1
import com.example.tensorflowliteapp.ml.EfficientdetLite2
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
    var model: Mobilenetv1

    constructor(context: Context){
        labels = FileUtil.loadLabels(context,"labels.txt")
        model0 = EfficientdetLite0.newInstance(context)
        model1 = EfficientdetLite1.newInstance(context)
        model2 = EfficientdetLite2.newInstance(context)
        model = Mobilenetv1.newInstance(context)
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(320,320, ResizeOp.ResizeMethod.BILINEAR)).build()
    }

    fun detect(bitmap: Bitmap): EfficientdetLite2.Outputs{
        var image = TensorImage.fromBitmap(bitmap)
        image = imageProcessor.process(image)
        val outputs = model2.process(image)
        return outputs
    }

    fun destroy(){
        model0.close()
        model1.close()
        model2.close()
        model.close()
    }
}