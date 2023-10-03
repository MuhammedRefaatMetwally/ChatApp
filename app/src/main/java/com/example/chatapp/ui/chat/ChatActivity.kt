package com.example.chatapp.ui.chat

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.Constants
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.model.Room

class ChatActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityChatBinding
    val viewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initViews()
        initParams()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.toastLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.newMessageLiveData.observe(this) {
            messagesAdapter.addNewMessages(it)
            viewBinding.content.messagesRv.smoothScrollToPosition(
                messagesAdapter.itemCount
            )
        }
    }

    private val messagesAdapter = MessagesAdapter(mutableListOf())
    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.content.messagesRv.adapter = messagesAdapter
        (viewBinding.content.messagesRv.layoutManager as LinearLayoutManager)
            .stackFromEnd = true
    }

    private fun initParams() {

        val room = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_ROOM, Room::class.java)
        } else {
            intent.getParcelableExtra(Constants.EXTRA_ROOM)
        }
        viewModel.changeRoom(room)
    }

}