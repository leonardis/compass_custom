package com.example.bellatrixtest

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private val compassView: CompassView by lazy {
        findViewById<CompassView>(R.id.compassView)
    }

    private val shakeDetector = ShakeDetector()
    private var sensorOrientation: Sensor? = null
    private var sensorAccelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensorOrientation?.let { sensor ->
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorAccelerometer?.let {
            shakeDetector.setOnShakeListener(object : ShakeDetector.OnShakeListener {
                override fun onShake(count: Int) {
                    // Here I update the compass with a random color.
                    val random = Random()
                    val color = Color.argb(
                        255,
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)
                    )
                    compassView.changeColor(color)
                }
            })
        }
    }
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            shakeDetector,
            sensorAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        sensorManager.unregisterListener(shakeDetector)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorOrientation?.let {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }


    private val sensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            val azimuth = event.values[0]
            compassView.updatePosition(azimuth)
        }
    }

}
