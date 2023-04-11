package com.example.tensorflowliteapp.message

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.example.tensorflowliteapp.ml.EfficientdetLite2


class PostProcessing :  SensorEventListener {

    val number : Int = 3
    var postionBrXText : Int = 0
    var postionBrYText : Int = 0
    var x : Float = 0F
    var y : Float = 0F
    var z : Float = 0F
    val threshold = 0.5

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

    fun gridPostion(
        detectionResult: EfficientdetLite2.DetectionResult
    ) : Array<Int?> {

            val location = detectionResult.locationAsRectF
            val category = detectionResult.categoryAsString
            val score = detectionResult.scoreAsFloat
            val centerObjectX = (location.left+location.right)/2
            val centerObjectY = (location.top+location.bottom)/2
            val gridCell = arrayOfNulls<Int>(2)
            val imageWidth = 320
            val imageHeight = 320


            //textView.text = "Location:top"+location.top+";\nbottom="+location.bottom+";\nright="+location.right+";\nleft="+location.left+";\n category="+category+
//                    ";\ncenterObjectX="+centerObjectX+"\ncenterObjectY="+centerObjectY

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
            /*var postion = 1
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
                }*/

            println("x="+x+";y="+y+";z="+z)



        //val classB = SecondFileTest()
        //textView.text = "Location:top"

        //val list = intent.getSerializableExtra("outputs") as ArrayList<String>
    }
        //Toast.makeText(MainActivity, list.joinToString(), Toast.LENGTH_SHORT).show()

    // Определя в каква позиция е телефона и с тези данни се казва къде е предмета, който търсим
    fun determingPhonePostion() {

        //txtKoltinAccelerometer.text = "up/down ${x.toInt()}\nleft/rigth ${y.toInt()}\nthirdPOstion ${z}"

    }


}



