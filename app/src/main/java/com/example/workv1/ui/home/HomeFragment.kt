package com.example.workv1.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.workv1.MainActivity
import com.example.workv1.R
import com.example.workv1.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


class HomeFragment() : Fragment() {

private var _binding: FragmentHomeBinding? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)



  // This property is only valid between onCreateView and
  // onDestroyView.

    var i = 0
    private val binding get() = _binding!!
    var day=0
    var week=0
    var month=0
    private var flagDay = 1

    private lateinit var countdownTimer: CountDownTimer
    private var remainingTimeInMillis: Long = 0
    private var isTimerRunning: Boolean = false

    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

      val healthDataManager = (requireActivity() as MainActivity).healthDataManager
//      val textView: TextView = binding.textHome
     var progressBar = binding.ringsHome.progressBar
      var  progressText = binding.ringsHome.progressText

      var progressBar2 = binding.ringsHome.progressBar2
      var progressBar3 = binding.ringsHome.progressBar3
      var  buttonDay= binding.ringsHome.dayButton
      var  buttonWeek= binding.ringsHome.weekButton
      var buttonMonth =binding.ringsHome.monthButton

        var btStartBoost =binding.boostHome.btStartBoost

      AnimationUtils.loadAnimation(requireContext(), R.anim.hyperspace_jump).also { hyperspaceJumpAnimation ->
        binding.adsHome.spaceshipImage.startAnimation(hyperspaceJumpAnimation)
      }


      buttonDay.setOnClickListener {
          flagDay=1
          val numberFormat: NumberFormat = DecimalFormat("#,##0")
          val formattedNumber: String = numberFormat.format(day)
          progressText.setText( formattedNumber)
          buttonDay.setTypeface(null, Typeface.BOLD)
          buttonWeek.setTypeface(null, Typeface.NORMAL)
          buttonMonth.setTypeface(null,Typeface.NORMAL)
      }
      buttonWeek.setOnClickListener {
          flagDay=2
          val numberFormat: NumberFormat = DecimalFormat("#,##0")
          val formattedNumber: String = numberFormat.format(week)
          progressText.setText( formattedNumber)
          buttonWeek.setTypeface(null, Typeface.BOLD)
          buttonDay.setTypeface(null, Typeface.NORMAL)
          buttonMonth.setTypeface(null,Typeface.NORMAL)
      }
      buttonMonth.setOnClickListener {
          flagDay=3
          val numberFormat: NumberFormat = DecimalFormat("#,##0")
          val formattedNumber: String = numberFormat.format(month)
          progressText.setText( formattedNumber)
          buttonMonth.setTypeface(null, Typeface.BOLD)
          buttonWeek.setTypeface(null, Typeface.NORMAL)
          buttonDay.setTypeface(null,Typeface.NORMAL)
      }

        btStartBoost.setOnClickListener {
            if (isTimerRunning) {
                pauseCountdownTimer()
                btStartBoost.setText("Start")
            } else {
                startCountdownTimer()
                btStartBoost.setText("Pause")
            }
        }

        if (savedInstanceState != null) {
            remainingTimeInMillis = savedInstanceState.getLong("remainingTime")
            if (remainingTimeInMillis > 0) {
                startCountdownTimer()
            }
        }

      mainScope.launch {
          healthDataManager.initializeHealthData()
            while (true) {

                try {
                    progressText!!.text = healthDataManager.step.toString()
//                    day=healthDataManager.readStepInputsForLastDay().sumBy { it.count.toInt() }
//                    val numberFormat: NumberFormat = DecimalFormat("#,##0")
//                    var formattedNumber: String = numberFormat.format(day)
//                    if(flagDay==1)
//                        progressText!!.text =  formattedNumber
//                    progressBar!!.progress=((day*100/10000))
//
//
//                    week=healthDataManager.readStepInputsForLastXDays(7).sumBy { it.count.toInt() }
//
//                    formattedNumber = numberFormat.format(week)
//                    if(flagDay==2)
//                        progressText!!.text =  formattedNumber
//                    progressBar2!!.progress=((week*100/(10000*7)))
//
//
//                    month=healthDataManager.readStepInputsForLastXDays(30).sumBy { it.count.toInt() }
//                    formattedNumber = numberFormat.format(month)
//                    if(flagDay==3)
//                        progressText!!.text =  formattedNumber
//                    progressBar3!!.progress=((month*100/(10000*30)))

                }catch (e:Exception)
                {

//                    textView.setText(e.message)
                }

                kotlinx.coroutines.delay(1000)
            }
      }


    homeViewModel.text.observe(viewLifecycleOwner) {
//      textView.text = it
    }
    return root
  }


    private fun startCountdownTimer() {
        if (!isTimerRunning) {
            // Calculate the initial time based on whether the timer is resumed
            val initialTimeInMillis = if (remainingTimeInMillis > 0) remainingTimeInMillis else 30 * 60 * 1000

            countdownTimer = object : CountDownTimer(initialTimeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    remainingTimeInMillis = millisUntilFinished
                    updateTimerUI()
                }

                override fun onFinish() {
                    remainingTimeInMillis = 0
                    updateTimerUI()
                    isTimerRunning = false
                }
            }

            countdownTimer.start()
            isTimerRunning = true
        }
    }

    private fun updateTimerUI() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTimeInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeInMillis) % 60
        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
       var timer = binding.boostHome.tvTimer
        timer.setText(formattedTime)
        // Update your UI element (e.g., a TextView) with the formattedTime
        // For example: timerTextView.text = formattedTime
    }
    private fun pauseCountdownTimer() {
        countdownTimer.cancel()
        updateTimerUI()
        isTimerRunning = false
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
try {
    countdownTimer?.cancel()
}catch (_:Exception)
{}


//   mainScope.cancel() // Cancel the coroutine scope to avoid leaks
    }
}