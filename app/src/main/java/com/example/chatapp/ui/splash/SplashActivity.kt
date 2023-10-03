package com.example.chatapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        subscribeToLiveData()
        Handler(mainLooper).postDelayed({
            viewModel.redirect()

        }, 2500)

    }

    private fun subscribeToLiveData() {
        viewModel.event.observe(this) {
            when (it) {
                SplashViewEvent.NavigateToLogin -> {
                    startLoginActivity()
                    finish()
                }

                SplashViewEvent.NavigateToHome -> {
                    startHomeActivity()
                    finish()
                }
            }
        }
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}