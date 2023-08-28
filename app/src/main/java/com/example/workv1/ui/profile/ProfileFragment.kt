package com.example.workv1.ui.profile

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.workv1.databinding.FragmentProifleBinding
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProifleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProifleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnChangeLanguage.setOnClickListener {
            changeLanguage(Locale("en"))
        }
        binding.btnChangeLanguage2.setOnClickListener {
            changeLanguage(Locale("ar"))
        }
        binding.btnChangeLanguage3.setOnClickListener {
            changeLanguage(Locale("iw"))
        }

        return root
    }

    private fun changeLanguage(locale: Locale) {
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale

        resources.updateConfiguration(config, resources.displayMetrics)

        requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
