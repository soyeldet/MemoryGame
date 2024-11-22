package com.example.memorygame.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.memorygame.R

class ImageAdapter2(private val items: MutableList<Int?>, private val items1: MutableList<Int?>) : BaseAdapter() {

    val correctAnswer = mutableListOf<Int>()

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val gridView: View
        val imageView: ImageView

        if (convertView == null) {
            val inflater = LayoutInflater.from(parent?.context)
            gridView = inflater.inflate(R.layout.grid_item, parent, false)
            imageView = gridView.findViewById(R.id.imageView)
            gridView.tag = imageView
        } else {
            gridView = convertView
            imageView = gridView.tag as ImageView
        }

        items[position]?.let { imageView.setImageResource(it) }
        return gridView
    }

    override fun getItem(position: Int): Int? {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addItem(item: Int) {
        if (correctAnswer.size < items.size) {
            items[correctAnswer.size] = item
            correctAnswer.add(item)
            notifyDataSetChanged()
        }
    }
}