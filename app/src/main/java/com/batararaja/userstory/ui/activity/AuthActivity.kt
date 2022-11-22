package com.batararaja.userstory.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.batararaja.userstory.Preferences
import com.batararaja.userstory.R
import com.batararaja.userstory.databinding.ActivityAuthBinding
import com.batararaja.userstory.ui.fragment.LoginFragment
import com.batararaja.userstory.ui.fragment.RegisterFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mFragmentManager = supportFragmentManager
        val mRegisterFragment = LoginFragment()
        val preferences = Preferences(this)
        val token = preferences.getToken()

        if (token.toString().isNotEmpty()) {
            val moveIntent = Intent(this@AuthActivity, ListStoryActivity::class.java)
            startActivity(moveIntent)
            finish()
        } else {
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, mRegisterFragment, LoginFragment::class.java.simpleName)
                .commit()
        }
    }
}