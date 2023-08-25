package com.example.workv1.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.workv1.MainActivity
import com.example.workv1.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)


  // This property is only valid between onCreateView and
  // onDestroyView.

    var i = 0
  private val binding get() = _binding!!



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
      val textView: TextView = binding.textHome

     var progressBar = binding.progressBar
      var  progressText = binding.progressText

//      val handler = Handler()
//      handler.postDelayed(object : Runnable {
//          override fun run() {
//              // set the limitations for the numeric
//              // text under the progress bar
//              if (i <= 100) {
//                  progressText!!.text = "" + i
//                  progressBar!!.progress = i
//                  i++
//                  handler.postDelayed(this, 200)
//              } else {
//                  handler.removeCallbacks(this)
//              }
//          }
//      }, 200)
      mainScope.launch {
          healthDataManager.initializeHealthData()
            while (true) {

                try {
                    var x=healthDataManager.readStepInputsForLastDay().sumBy { it.count.toInt() }
                   textView.setText(x.toString())
                    progressText!!.text = ((x*100/10000)).toString()+" _1"
                    progressBar!!.progress=((x*100/10000))
                }catch (e:Exception)
                {

                    textView.setText(e.message)
                }

                kotlinx.coroutines.delay(1000) // Delay for 10 seconds before the next update
            }
      }


    homeViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//    mainScope.cancel() // Cancel the coroutine scope to avoid leaks
    }
}