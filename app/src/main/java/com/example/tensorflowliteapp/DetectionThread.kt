package com.example.tensorflowliteapp

class DetectionThread(objectDetector: ObjectDetector, text2Speech: Text2Speech, mode: Int): Runnable {
    var mode = mode
    var look4object = ""
    override fun run() {
        log(this.mode.toString())
    }
}