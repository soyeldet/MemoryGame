package com.example.memorygame.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.memorygame.R

class ImageAdapterAvatarsGV(private val items: MutableList<Int?>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent?.context)
            view = inflater.inflate(R.layout.avatar_icon_layout, parent, false)
        }

        val imageView: ImageView = view?.findViewById(R.id.imageView) ?: return view!!

        if (items[position] == R.color.light_gray){
            imageView.isEnabled = false
            imageView.setOnClickListener(null)
        } else {
            imageView.setImageResource(items[position]!!)
        }
        imageView.setImageResource(items[position]!!)

        return view
    }
}

