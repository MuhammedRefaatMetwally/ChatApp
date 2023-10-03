package com.example.chatapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemRoomBinding
import com.example.chatapp.model.Category
import com.example.chatapp.model.Room

class RoomsRecyclerAdapter(var rooms: List<Room?>? = emptyList()) :
    RecyclerView.Adapter<RoomsRecyclerAdapter.ViewHolder>() {


    inner class ViewHolder(private val itemBinding: ItemRoomBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(room: Room?) {
            itemBinding.catImage.setImageResource(Category.getCategoryImageByCategoryId(room?.categoryId))
            itemBinding.title.text = room?.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return rooms?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rooms?.get(position))
        onItemClickListener?.let {
            holder.itemView.setOnClickListener { view ->
                it.onItemClick(position, rooms!![position])
            }
        }
    }

    fun changeData(rooms: List<Room?>?) {
        this.rooms = rooms
        notifyDataSetChanged()
    }

    var onItemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, room: Room?)
    }
}