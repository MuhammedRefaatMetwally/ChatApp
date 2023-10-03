package com.example.chatapp.common

import androidx.recyclerview.widget.DiffUtil
import com.example.chatapp.model.Room

class RoomsCallback(
    private val oldList: MutableList<Room?>?,
    private val newList: MutableList<Room>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList?.get(oldItemPosition)?.id === newList.get(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, value, name) = oldList?.get(oldItemPosition) ?: Room()
        val (_, value1, name1) = newList[newItemPosition]
        return name == name1 && value == value1
    }


}
