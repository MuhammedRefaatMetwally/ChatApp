package com.example.chatapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.chatapp.SessionProvider
import com.example.chatapp.databinding.ItemReceieveMessageBinding
import com.example.chatapp.databinding.ItemSentMessageBinding
import com.example.chatapp.model.Message

class MessagesAdapter(var messages: MutableList<Message>) : Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (message.senderId == SessionProvider.user?.id) {
            return MessageType.Sent.value
        } else {
            return MessageType.Receive.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MessageType.Sent.value) {
            val itemBinding =
                ItemSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return SendMessageViewHolder(itemBinding)
        } else {
            val itemBinding = ItemReceieveMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ReceiverMessageViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SendMessageViewHolder -> {
                holder.bind(messages[position]) //smart casting
            }

            is ReceiverMessageViewHolder -> {
                holder.bind(messages[position]) //smart casting
            }
        }
    }

    fun addNewMessages(newMessages: List<Message>?) {
        if (newMessages == null) return
        val oldSize = messages.size
        messages.addAll(newMessages)
        notifyItemRangeInserted(oldSize, newMessages.size)
    }
}


class SendMessageViewHolder(val itemBinding: ItemSentMessageBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(message: Message) {
        itemBinding.setMessage(message)
        itemBinding.executePendingBindings()// 3shan yforce bind data abl my recycle
    }
}

class ReceiverMessageViewHolder(val itemBinding: ItemReceieveMessageBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(message: Message) {
        itemBinding.setMessage(message)
        itemBinding.executePendingBindings()// 3shan yforce bind data abl my recycle
    }
}


enum class MessageType(val value: Int) {
    Sent(100),
    Receive(200)
}