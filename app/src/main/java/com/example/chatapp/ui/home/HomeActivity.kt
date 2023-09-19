package com.example.chatapp.ui.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityHomeBinding
import com.example.chatapp.ui.add_room.AddRoomActivity
import com.example.chatapp.ui.login.LoginActivity
import com.example.chatapp.ui.showLoadingProgressDialog
import com.example.chatapp.ui.showMessage

class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initViews()
        subscribeToLiveData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getRooms()
    }

    private var loadingDialog: ProgressDialog? = null
    private fun subscribeToLiveData() {
        viewModel.events.observe(this, ::handleEvents)

        viewModel.roomsList.observe(this) {
            adapter.changeData(it)
        }

        viewModel.messageLiveData.observe(this) {
            showMessage(
                message = it.message ?: "Something Went Wrong",
                posActionName = it.posActionName,
                posAction = it.onPosActionClick,
                negActionName = it.negActionName,
                negAction = it.onNegActionClick
            )
        }

        viewModel.loadingLiveData.observe(this) {
            if (it == null) {
                loadingDialog?.dismiss()
                loadingDialog = null
                return@observe
            }
            loadingDialog = showLoadingProgressDialog(
                it.message ?: "Something Went Wrong",
                isCancelable = it.isCancelable
            )
            loadingDialog?.show()
        }

    }

    private fun handleEvents(homeViewEvent: HomeViewEvent?) {
        when (homeViewEvent) {
            HomeViewEvent.NavigateToAddRoom -> {
                navigateToAddRoom()
            }

            HomeViewEvent.NavigateToLogin -> {
                navigateToLogin()
            }

            else -> {}
        }

    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToAddRoom() {
        val intent = Intent(this, AddRoomActivity::class.java)
        startActivity(intent)
    }

    private val adapter: RoomsRecyclerAdapter = RoomsRecyclerAdapter()
    private fun initViews() {
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.content.roomsRv.adapter = adapter
    }
}