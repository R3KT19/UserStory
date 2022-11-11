package com.batararaja.userstory.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.batararaja.userstory.MainViewModel
import com.batararaja.userstory.R
import com.batararaja.userstory.RegisterResponse
import com.batararaja.userstory.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        binding.tvLoginAccount.setOnClickListener {
            changeFragment()
        }
        binding.btnRegister.setOnClickListener {
            mainViewModel.register(binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString())
            mainViewModel.register.observe(viewLifecycleOwner, {data->
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
        setupAction()
        playAnimation()
        return binding.root
    }

    private fun showMessage(data: RegisterResponse?) {
        if (data?.error == false) {
            changeFragment()
        } else {
            Toast.makeText(
                requireActivity(),
                data?.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    private fun changeFragment() {
        val loginFragment = LoginFragment()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, loginFragment, LoginFragment::class.java.simpleName)
            .commit()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val text1 = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val text2 = ObjectAnimator.ofFloat(binding.tvLoginAccount, View.ALPHA, 1f).setDuration(500)
        val btn = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                name,
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