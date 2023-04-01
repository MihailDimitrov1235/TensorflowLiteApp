package com.example.tensorflowliteapp

class distanceCalulator {
    val close = mapOf(
        "person" to 0.24,
        "bicycle" to 0.40,
        "car" to 0.60,
        "motorcycle" to 0.40,
        "airplane" to 0.80,
        "bus" to 0.65,
        "train" to 0.65,
        "truck" to 0.70,
        "boat" to 0.45,
        "traffic light" to 0.20,
        "fire hydrant" to 0.10,
        "stop sign" to 0.15,
        "parking meter" to 0.15,
        "bench" to 0.25,
        "bird" to 0.15,
        "cat" to 0.20,
        "dog" to 0.25,
        "horse" to 0.50,
        "sheep" to 0.35,
        "cow" to 0.45,
        "elephant" to 0.75,
        "bear" to 0.65,
        "zebra" to 0.35,
        "giraffe" to 0.45,
        "backpack" to 0.10,
        "umbrella" to 0.15,
        "handbag" to 0.20,
        "tie" to 0.05,
        "suitcase" to 0.20,
        "frisbee" to 0.10,
        "skis" to 0.20,
        "snowboard" to 0.20,
        "sports ball" to 0.10,
        "kite" to 0.25,
        "baseball bat" to 0.10,
        "baseball glove" to 0.05,
        "skateboard" to 0.20,
        "surfboard" to 0.20,
        "tennis racket" to 0.20,
        "bottle"  to 0.03,
        "wine glass" to 0.05,
        "cup" to 0.05,
        "fork" to 0.05,
        "knife" to 0.05,
        "spoon" to 0.05,
        "bowl" to 0.05,
        "banana" to 0.05,
        "apple" to 0.05,
        "sandwich" to 0.05,
        "orange" to 0.05,
        "broccoli" to 0.05,
        "carrot" to 0.05,
        "hot dog" to 0.05,
        "pizza" to 0.15,
        "donut" to 0.05,
        "cake" to 0.10,
        "chair" to 0.20,
        "couch" to 0.35,
        "potted plant" to 0.25,
        "bed" to 0.40,
        "dining table" to 0.35,
        "toilet" to 0.20,
        "tv" to 0.30,
        "laptop" to 0.25,
        "mouse" to 0.05,
        "remote" to 0.05,
        "keyboard" to 0.15,
        "cell phone" to 0.05,
        "microwave" to 0.15,
        "oven" to 0.20,
        "toaster" to 0.10,
        "sink" to 0.15,
        "refrigerator" to 0.30,
        "book" to 0.05,
        "clock" to 0.10,
        "vase" to 0.05,
        "scissors" to 0.05,
        "teddy bear" to 0.05,
        "hair drier" to 0.15,
        "toothbrush" to 0.05,
    )
    val medium = mapOf(        
        "person" to 0.05,
        "bicycle" to 0.20,
        "car" to 0.30,
        "motorcycle" to 0.20,
        "airplane" to 0.40,
        "bus" to 0.325,
        "train" to 0.325,
        "truck" to 0.35,
        "boat" to 0.225,
        "traffic light" to 0.10,
        "fire hydrant" to 0.05,
        "stop sign" to 0.075,
        "parking meter" to 0.075,
        "bench" to 0.125,
        "bird" to 0.075,
        "cat" to 0.10,
        "dog" to 0.125,
        "horse" to 0.25,
        "sheep" to 0.175,
        "cow" to 0.225,
        "elephant" to 0.375,
        "bear" to 0.325,
        "zebra" to 0.175,
        "giraffe" to 0.225,
        "backpack" to 0.018,
        "umbrella" to 0.075,
        "handbag" to 0.10,
        "tie" to 0.025,
        "suitcase" to 0.10,
        "frisbee" to 0.05,
        "skis" to 0.10,
        "snowboard" to 0.10,
        "sports ball" to 0.05,
        "kite" to 0.125,
        "baseball bat" to 0.05,
        "baseball glove" to 0.025,
        "skateboard" to 0.10,
        "surfboard" to 0.10,
        "tennis racket" to 0.10,
        "bottle"  to 0.0018,
        "wine glass" to 0.025,
        "cup" to 0.025,
        "fork" to 0.025,
        "knife" to 0.025,
        "spoon" to 0.025,
        "bowl" to 0.025,
        "banana" to 0.025,
        "apple" to 0.025,
        "sandwich" to 0.025,
        "orange" to 0.025,
        "broccoli" to 0.025,
        "carrot" to 0.025,
        "hot dog" to 0.025,
        "pizza" to 0.075,
        "donut" to 0.025,
        "cake" to 0.05,
        "chair" to 0.10,
        "couch" to 0.175,
        "potted plant" to 0.125,
        "bed" to 0.20,
        "dining table" to 0.175,
        "toilet" to 0.10,
        "tv" to 0.15,
        "laptop" to 0.125,
        "mouse" to 0.025,
        "remote" to 0.025,
        "keyboard" to 0.075,
        "cell phone" to 0.025,
        "microwave" to 0.075,
        "oven" to 0.10,
        "toaster" to 0.05,
        "sink" to 0.075,
        "refrigerator" to 0.15,
        "book" to 0.025,
        "clock" to 0.05,
        "vase" to 0.025,
        "scissors" to 0.025,
        "teddy bear" to 0.025,
        "hair drier" to 0.075,
        "toothbrush" to 0.025
    )
    fun distance(word : String, size : Float, lang : String): String {
        if(size >= close[word]!!){
            if(lang == "en"){
                return "close"
            }else{
                return "близо"
            }
        }else if(size >= medium[word]!!){
            if(lang == "en"){
                return "at a medium distance"
            }else{
                return "на средно разстояние"
            }
        }else {
            if (lang == "en"){
                return "far"
            }else{
                return "далеч"
            }

        }
    }

}