package com.example.tensorflowliteapp.message

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.example.tensorflowliteapp.Result
import com.example.tensorflowliteapp.message.*

class PostProcessing :  SensorEventListener {

    var x : Float = 0F
    var y : Float = 0F
    var z : Float = 0F
    @JvmName("getX1")
    fun getX(): Float {
        return this.x;
    }
    @JvmName("getY1")
    fun getY(): Float {
        return this.y;
    }
    @JvmName("getZ1")
    fun getZ(): Float {
        return this.z;
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            x = event.values[0]
            y = event.values[1]
            z = event.values[2]
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }


}



