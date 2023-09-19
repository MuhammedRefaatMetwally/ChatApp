package com.example.chatapp.ui.add_room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.chatapp.databinding.ItemRoomCategoryBinding
import com.example.chatapp.model.Category

class RoomCategoriesAdapter(val items: List<Category>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder

        if (convertView == null) {
            val itemBinding =
                ItemRoomCategoryBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            viewHolder = ViewHolder(itemBinding)
            itemBinding.root.tag = viewHolder // trbot el view bl viewholder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.bind(items[position])
        return viewHolder.itemBiding.root
    }

    inner class ViewHolder(val itemBiding: ItemRoomCategoryBinding) {
        fun bind(item: Category) {
            itemBiding.image.setImageResource(item.imageResId)
            itemBiding.title.text = item.title
        }
    }
}