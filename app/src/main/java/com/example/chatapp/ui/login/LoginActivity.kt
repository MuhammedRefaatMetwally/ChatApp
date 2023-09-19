package com.example.chatapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityLoginBinding
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.ui.register.RegisterActivity
import com.example.chatapp.ui.showMessage

class LoginActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()

    }

    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewBinding.lifecycleOwner = this
        viewBinding.vm = viewModel

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.messageLiveData.observe(this) { message ->
            showMessage(
                message = message.message ?: "Something Went Wrong",
                posActionName = message.posActionName,
                posAction = message.onPosActionClick,
                negAction = message.onNegActionClick,
                negActionName = message.negActionName,
                isCancelable = message.isCancelable
            )
        }

        viewModel.events.observe(this, ::handleEvents)
    }

    private fun handleEvents(loginViewEvent: LoginViewEvent) {
        when (loginViewEvent) {
            LoginViewEvent.NavigationToHome -> {
                navigateToHome()
            }

            LoginViewEvent.NavigationToRegister -> {
                navigateToRegister()
            }
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}