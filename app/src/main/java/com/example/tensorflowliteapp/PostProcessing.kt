package com.example.tensorflowliteapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.widget.TextView
import com.example.tensorflowliteapp.ml.EfficientdetLite2


class PostProcessing :  SensorEventListener {

    val number : Int = 3
    var postionBrXText : Int = 0
    var postionBrYText : Int = 0
    var x : Float = 0F
    var y : Float = 0F
    var z : Float = 0F
//    var output : Outputs = TODO()
    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            x = event.values[0]
            y = event.values[1]
            z = event.values[2]
            var postProcessingObj = PostProcessing();

            //postProcessingObj.determingPhonePostion(x,y,z);
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

    fun postProccessingInfo(
        outputs: EfficientdetLite2.Outputs,
        textView: TextView
    ) {
        //output = outputs
        outputs.detectionResultList.forEachIndexed { index, detectionResult ->
            val location = detectionResult.locationAsRectF
            val category = detectionResult.categoryAsString
            val score = detectionResult.scoreAsFloat
            val centerObjectX = (location.left+location.right)/2
            val centerObjectY = (location.top+location.bottom)/2

            //textView.text = "Location:top"+location.top+";\nbottom="+location.bottom+";\nright="+location.right+";\nleft="+location.left+";\n category="+category+
//                    ";\ncenterObjectX="+centerObjectX+"\ncenterObjectY="+centerObjectY

            if(centerObjectX>0 && centerObjectX<=60){
                postionBrXText = 1
             //   textView.text = "Po X e mejdu 0 i 60;\ncategory="+category+";\nX="+x+"\nY="+y+"\nZ="+z
            }else if(centerObjectX>60 && centerObjectX<=120){
                postionBrXText = 2
            }else if(centerObjectX>120 && centerObjectX<=180){
                postionBrXText = 3
            }else if(centerObjectX>180 && centerObjectX<=240){
                postionBrXText = 4
            }else if(centerObjectX>240 && centerObjectX<=300){
                postionBrXText = 5
            }
            if(centerObjectY>0 && centerObjectY<=60){
              //  textView.text = "Po Y e mejdu 0 i 60;\ncategory="+category
                postionBrYText = 1
            }else if(centerObjectY>60 && centerObjectY<=120){
                postionBrYText = 2
            }else if(centerObjectY>120 && centerObjectY<=180){
                postionBrYText = 3
            }else if(centerObjectY>180 && centerObjectY<=240){
                postionBrYText = 4
            }else if(centerObjectY>240 && centerObjectY<=300){
                postionBrYText = 5
            }

            var postion = 1
            if(x>=0 && x<=10){
                if(z >= 0 && z <= 2.5){
                    postion = 1;
                    //return postion;
                }else if(z>2.5 && z<=5){
                    postion = 2;
                    //return postion;
                }else if(z>5 && z<=7.5){
                    postion = 3;
                    //return postion;
                }else if(z>7.5 && z<=10){
                    postion = 4;
                    //return postion;
                }
            }
            println("x="+x+";y="+y+";z="+z)
            //textView.text = "Po X e mejdu 0 i 60;\ncategory="+category+";\nX="+x+"\nY="+y+"\nZ="+z
  //          textView.text = "Location:top"+location.top+";\nbottom="+location.bottom+";\nright="+location.right+";\nleft="+location.left+";\n category="+category+
    //                ";\ncenterObjectX="+centerObjectX+"\ncenterObjectY="+centerObjectY
            //if(location.top){

            //}

        }
        //val classB = SecondFileTest()
        //textView.text = "Location:top"

        //val list = intent.getSerializableExtra("outputs") as ArrayList<String>
        println("hehe")
        //Toast.makeText(MainActivity, list.joinToString(), Toast.LENGTH_SHORT).show()
    }
    // Определя в каква позиция е телефона и с тези данни се казва къде е предмета, който търсим
    fun determingPhonePostion() {

        //txtKoltinAccelerometer.text = "up/down ${x.toInt()}\nleft/rigth ${y.toInt()}\nthirdPOstion ${z}"

    }


}



