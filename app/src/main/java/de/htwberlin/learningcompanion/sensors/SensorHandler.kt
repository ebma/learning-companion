package de.htwberlin.learningcompanion.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class SensorHandler(private val sensorManager: SensorManager) : SensorEventListener, AnkoLogger {

    private val DEFAULT_TIME_BETWEEN_DATA_EVENTS = 0.2 // average time during sensor events

    private var intervalInSeconds: Int = 0
    private var intervalCounter = 0

    private var lightSensor: Sensor? = null

    val dataList = arrayListOf<Float>()

    fun start(intervalInSeconds: Int) {
        this.intervalInSeconds = intervalInSeconds
        intervalCounter = intervalInSeconds * 10 // make interval counter high enough so that the first value is collected

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        info { "$sensor and $accuracy" }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == lightSensor) {
            intervalCounter += 1

            if (intervalCounter * DEFAULT_TIME_BETWEEN_DATA_EVENTS >= intervalInSeconds) {
                dataList.add(event.values[0])
                intervalCounter = 0
                info { "${event.values[0]} added" }
            }
        }
    }
}