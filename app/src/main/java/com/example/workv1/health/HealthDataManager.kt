package com.example.workv1.health

import android.content.Context
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.FragmentActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.workv1.SavingDataManagement
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class HealthDataManager(private val context: Context) {
    var isOK = false

    lateinit var savingDataManagement:SavingDataManagement;
    var step= 0
    var coin= 0



//    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }
//    private val permissions = setOf(
//        HealthPermission.getReadPermission(StepsRecord::class),
//        HealthPermission.getWritePermission(StepsRecord::class)
//    )

    suspend fun initializeHealthData() {
        savingDataManagement= SavingDataManagement(context)
        step=savingDataManagement.lastData.step
        coin=savingDataManagement.lastData.coin
//        if (!hasAllPermissions(permissions)) {
//
//            requestPermission.launch(permissions)
//        }else
//        {
            isOK=true
//        }
    }

//    suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
//        try {
//            return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
//        }
//      catch (e:Exception)
//      {
//          return false;
//      }
//    }

//    val requestPermission = (context as FragmentActivity).registerForActivityResult(
//        PermissionController.createRequestPermissionResultContract()
//    ) { grantedPermissions: Set<String> ->
//        if (
//            grantedPermissions.contains(HealthPermission.getReadPermission(StepsRecord::class))
//        ) {
//            // Read or process steps related health records.
//
//            Toast.makeText(context, "All permissions granted", Toast.LENGTH_SHORT).show()
//            isOK=true
//            // You can also call the function to read and process the health records here
//            // healthDataManager.readAndProcessHealthRecords()
//        } else {
//            // User denied permission
//            Toast.makeText(context, "Not all permissions granted", Toast.LENGTH_SHORT).show()
//            isOK=false
//        }
//    }


//    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
//        return PermissionController.createRequestPermissionResultContract()
//    }
//
//    suspend fun readStepInputsForLastDay(): List<StepsRecord> {
//        val now = Instant.now()
//        val timeZone = ZoneId.systemDefault()
//        val startOfToday = ZonedDateTime.ofInstant(now, timeZone).toLocalDate().atStartOfDay(timeZone).toInstant()
//        val startOfTomorrow = ZonedDateTime.ofInstant(now, timeZone).plusDays(1).toLocalDate().atStartOfDay(timeZone).toInstant()
//        return readStepInputs(startOfToday, startOfTomorrow)
//    }
//
//    suspend fun readStepInputsForLastXDays(x: Long): List<StepsRecord> {
//        val now = Instant.now()
//        val timeZone = ZoneId.systemDefault()
//
//        // Calculate the start date by subtracting 'x' days from the current date
//        val startOfXDaysAgo = ZonedDateTime.ofInstant(now, timeZone).minusDays(x-1).toLocalDate().atStartOfDay(timeZone).toInstant()
//
//        // Calculate the end date as the current date
//        val endOfToday = ZonedDateTime.ofInstant(now, timeZone).plusDays(1).toLocalDate().atStartOfDay(timeZone).toInstant()
//
//        return readStepInputs(startOfXDaysAgo, endOfToday)
//    }

//    suspend fun readStepInputs(start: Instant, end: Instant): List<StepsRecord> {
////        try {
//            if (isOK) {
//                val request = ReadRecordsRequest(
//                    recordType = StepsRecord::class,
//                    timeRangeFilter = TimeRangeFilter.between(start, end)
//                )
//                val response = healthConnectClient.readRecords(request)
//                return response.records
//            } else {
//                throw IllegalStateException("Health is not OK")
//            }
////        } catch (e: Exception) {
////            throw Exception("Error reading step records", e)
////        }
//    }



    private  fun showToast(message: String) {
//        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        }
    }
}