package com.example.workv1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.workv1.MainActivity
import com.example.workv1.databinding.FragmentHomeBinding
import com.example.workv1.health.HealthDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)


  // This property is only valid between onCreateView and
  // onDestroyView.
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

      mainScope.launch {
          healthDataManager.initializeHealthData()
            while (true) {

                try {
                   textView.setText( healthDataManager.readStepInputsForLastDay().sumBy { it.count.toInt() }.toString())
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