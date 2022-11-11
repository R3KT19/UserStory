package com.batararaja.userstory.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.batararaja.userstory.*
import com.batararaja.userstory.Preferences
import com.batararaja.userstory.databinding.FragmentLoginBinding
import com.batararaja.userstory.ui.activity.ListStoryActivity


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            mainViewModel.login(binding.edLoginEmail.text.toString(),
                binding.edLoginPassword.text.toString())
            mainViewModel.login.observe(viewLifecycleOwner, {data ->
                showMessage(data)
            })
            mainViewModel.isLoading.observe(viewLifecycleOwner, {
                showLoading(it)
            })
            mainViewModel.message.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(
                        activity,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        binding.tvCreateAccount.setOnClickListener {
            val registerFragment = RegisterFragment()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, registerFragment, RegisterFragment::class.java.simpleName)
                .commit()
        }
        setupAction()
        playAnimation()
        return binding.root
    }

    private fun showMessage(data: LoginResponse?) {
        if (data?.error == false) {
            val preferences = Preferences(requireActivity())
            preferences.setToken(data.loginResult.token)
            moveToListStory()
        } else {
            Toast.makeText(
                requireActivity(),
                data?.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun moveToListStory() {
        val intent = Intent(activity, ListStoryActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val text1 = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, 1f).setDuration(500)
        val text2 = ObjectAnimator.ofFloat(binding.tvCreateAccount, View.ALPHA, 1f).setDuration(500)
        val btn = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                email,
                password,
                text1,
                text2,
                btn
            )
            startDelay = 500
        }.start()
    }
}