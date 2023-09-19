package com.example.chatapp.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityRegisterBinding
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.ui.login.LoginActivity
import com.example.chatapp.ui.showMessage

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()

    }

    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_register
        )

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
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

    private fun handleEvents(registerViewEvent: RegisterViewEvent) {
        when (registerViewEvent) {
            RegisterViewEvent.NavigationToHome -> {
                navigateToHome()
            }

            RegisterViewEvent.NavigationToLogin -> {
                navigateToLogin()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}