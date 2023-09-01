package com.example.workv1.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.workv1.Constants
import com.example.workv1.Constants.Companion.BOOST_TIME
import com.example.workv1.Constants.Companion.DAYS_IN_MONTH
import com.example.workv1.Constants.Companion.DAYS_IN_WEEK
import com.example.workv1.MainActivity
import com.example.workv1.R
import com.example.workv1.SavingDataManagement
import com.example.workv1.SharedViewModel
import com.example.workv1.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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


    private lateinit var savingDataManagement:SavingDataManagement

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
        val progressBar = binding.ringsHome.progressBar
        val progressText = binding.ringsHome.progressText
        val tvCoin =binding.ringsHome.tvHomeCoin

        val progressBar2 = binding.ringsHome.progressBar2
        val progressBar3 = binding.ringsHome.progressBar3
        val buttonDay= binding.ringsHome.dayButton
        val buttonWeek= binding.ringsHome.weekButton
        val buttonMonth =binding.ringsHome.monthButton
        val btStartBoost =binding.boostHome.btStartBoost

        AnimationUtils.loadAnimation(requireContext(), R.anim.hyperspace_jump).also { hyperspaceJumpAnimation ->
            binding.adsHome.spaceshipImage.startAnimation(hyperspaceJumpAnimation)
        }

        savingDataManagement= SavingDataManagement(requireContext())

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
//                    progressText.text = healthDataManager.step.toString()
//                    day=healthDataManager.readStepInputsForLastDay().sumBy { it.count.toInt() }
                    day= healthDataManager.step
                    val numberFormat: NumberFormat = DecimalFormat("#,##0")
                    var formattedNumber: String = numberFormat.format(day)
                    if(flagDay==1)
                        progressText.text =  formattedNumber
                    progressBar.progress=((day*100f/Constants.DAY_GOAL_STEP))
                    tvCoin.setText(healthDataManager.coin.toString())

//                    week=healthDataManager.readStepInputsForLastXDays(7).sumBy { it.count.toInt() }
                    week=savingDataManagement.sumOfALlStepLastXDays(Constants.DAYS_IN_WEEK)
                    formattedNumber = numberFormat.format(week)
                    if(flagDay==2)
                        progressText.text =  formattedNumber
                    progressBar2.progress=((week*100f/(Constants.DAY_GOAL_WEEK)))


                //    month=healthDataManager.readStepInputsForLastXDays(30).sumBy { it.count.toInt() }
                    month=savingDataManagement.sumOfALlStepLastXDays(DAYS_IN_MONTH)
                    formattedNumber = numberFormat.format(month)
                    if(flagDay==3)
                        progressText.text =  formattedNumber
                    progressBar3.progress=((month*100f/(Constants.DAY_GOAL_MONTH)))

                    savingDataManagement.addStepAndSave(day,isTimerRunning)
                    tvCoin.text= savingDataManagement.getCoin(day,isTimerRunning).toString()
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
            val initialTimeInMillis = if (remainingTimeInMillis > 0) remainingTimeInMillis else BOOST_TIME * 60 * 1000

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
       val timer = binding.boostHome.tvTimer
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
        val savingDataManagement =SavingDataManagement(requireContext())
        savingDataManagement.addStepAndSave(day,isTimerRunning)
        super.onDestroyView()
            _binding = null
        try {
            countdownTimer.cancel()
        }catch (_:Exception) {}

    }
    override fun onPause() {
        super.onPause()
        savingDataManagement.addStepAndSave(day,isTimerRunning)
    }

}