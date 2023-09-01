package com.example.workv1.ui.profile

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.workv1.Constants
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
        binding.editTextText.setText(Constants.DAY_GOAL_STEP.toString())
        val editText: EditText = binding.editTextText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                // This method is called when the text changes
            }

            override fun afterTextChanged(editable: Editable?) {
                val newText = editable.toString()
                val newValue: Int = newText.toIntOrNull() ?: 0
                // This method is called after the text has changed
//                Constants.DAY_GOAL_STEP=newValue
            }
        })
        binding.editTextText.onFocusChangeListener
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

    override fun onPause() {
        super.onPause()
    }
}
