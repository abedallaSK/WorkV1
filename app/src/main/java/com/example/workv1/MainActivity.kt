package com.example.workv1

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.workv1.Data.dateFormat
import com.example.workv1.databinding.ActivityMainBinding
import com.example.workv1.health.HealthDataManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Date


class MainActivity : AppCompatActivity() , SensorEventListener {

private lateinit var binding: ActivityMainBinding

    var healthDataManager: HealthDataManager  = HealthDataManager(this)
    lateinit var savingDataManagement: SavingDataManagement;


    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalStep: Float = 0f

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_ACTIVITY_RECOGNITION = 2
        private const val TAG = "CountStep"
    }

    private var activityRecognitionGranted = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        savingDataManagement=SavingDataManagement(this)
         //

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)


            val navView: BottomNavigationView = binding.navView
            setSupportActionBar(binding.homeToolbar)
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
//        setSupportActionBar(binding.homeToolbar)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_wallet,
                    R.id.navigation_notifications,
                    R.id.navigation_profile
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


        } catch (e: Exception) {
                System.out.println(e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }



    private fun activityRecognitionPermission() {
        Log.v(TAG, "version sdk : ${Build.VERSION.SDK_INT} and version code : ${Build.VERSION_CODES.Q}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_DENIED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                    PERMISSIONS_REQUEST_ACCESS_ACTIVITY_RECOGNITION
                )
            } else {
                activityRecognitionGranted = true
            }
        } else {
            Log.v(TAG, "seftpermis ${ContextCompat.checkSelfPermission(this, "com.google.android.gms.permission.ACTIVITY_RECOGNITION")}")

            if (ContextCompat.checkSelfPermission(
                    this,
                    "com.google.android.gms.permission.ACTIVITY_RECOGNITION"
                ) == PackageManager.PERMISSION_DENIED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf("com.google.android.gms.permission.ACTIVITY_RECOGNITION"),
                    PERMISSIONS_REQUEST_ACCESS_ACTIVITY_RECOGNITION
                )
            } else {
                activityRecognitionGranted = true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        activityRecognitionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_ACTIVITY_RECOGNITION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activityRecognitionGranted = true
                    startSensor()
                } else {
                    Toast.makeText(this, "This activity need permission to use", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }



    override fun onResume() {
        super.onResume()
        running = true
        activityRecognitionPermission()
        val x=savingDataManagement.getData(Date())
        healthDataManager.step=x.step
        Log.v(TAG, "permission : $activityRecognitionGranted")

        startSensor()
    }


    private fun startSensor() {
        if (activityRecognitionGranted) {
            val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            if (stepSensor == null) {
                Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
            } else {
                sensorManager?.registerListener(
                    this,
                    stepSensor,
                    SensorManager.SENSOR_DELAY_FASTEST
                )
                Toast.makeText(this, "Set up monitor!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Toast.makeText(this, "Counting!!!", Toast.LENGTH_SHORT).show()
        if (p0!!.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            if (running) {
                healthDataManager.step += p0.values[0].toInt()
                healthDataManager.coin=savingDataManagement.getCoin( healthDataManager.step)
                savingDataManagement.addStepAndSave(healthDataManager.step)
//                healthDataManager.step= totalStep.toInt()
                Log.v(TAG, "Sensor value: $totalStep")
                Toast.makeText(this, "Counting!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}