package com.example.teuxdeux.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.teuxdeux.DarkModeViewModel
import com.example.teuxdeux.R


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString("email")
        }
    }

    lateinit var viewModel: DarkModeViewModel

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        viewModel = ViewModelProvider(this).get(DarkModeViewModel::class.java)

        viewModel.darkModeUnitState.observe(viewLifecycleOwner, Observer {
            AppCompatDelegate.setDefaultNightMode(it)
        })

        val switch: Switch = view.findViewById(R.id.themeSwitch)

        switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                viewModel.darkModeUnitState.value = 2 // MODE_NIGHT_YES

            } else {
                viewModel.darkModeUnitState.value = 1 // MODE_NIGHT_NO
            }
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param email Email Address of user
         **/

        @JvmStatic
        fun newInstance(email: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString("email", email)
                }
            }
    }


}