package de.htwberlin.learningcompanion.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaRecorder
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.IOException

class SensorHandler(private val sensorManager: SensorManager) : SensorEventListener, AnkoLogger {

    private val DEFAULT_TIME_BETWEEN_DATA_EVENTS = 0.2 // average time during sensor events

    private var intervalInSeconds: Int = 0
    private var eventCounter = 0

    private var lightSensor: Sensor? = null

    val dataList = arrayListOf<Float>()

    private var mRecorder: MediaRecorder? = null

    private var running = false

    fun start(intervalInSeconds: Int) {
        this.intervalInSeconds = intervalInSeconds

        if (!running) {
            startLightSensor()
            startRecording()
            running = true
        }
    }

    private fun startLightSensor() {
        eventCounter = intervalInSeconds * 10 // make eventcounter high enough so that the first value is immediately collected on start

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun startRecording() {
        mRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile("/dev/null")
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {

            }

            start()
        }
    }

    fun stop() {
        if (running) {
            sensorManager.unregisterListener(this)

            stopRecording()
            running = false
        }
    }

    private fun stopRecording() {
        mRecorder?.apply {
            stop()
            release()
        }
        mRecorder = null
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        info { "$sensor and $accuracy" }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == lightSensor) {
            eventCounter += 1

            if (eventCounter * DEFAULT_TIME_BETWEEN_DATA_EVENTS >= intervalInSeconds) {
                onCollectData(event)
            }
        }
    }

    private fun onCollectData(event: SensorEvent) {
        dataList.add(event.values[0])
        eventCounter = 0
        info { "${event.values[0]} added" }
        info { "Amplitude: ${getAmplitude()}" }
    }

    private fun getAmplitude(): Int {
        return if (mRecorder != null)
            mRecorder!!.maxAmplitude
        else
            0
    }


}