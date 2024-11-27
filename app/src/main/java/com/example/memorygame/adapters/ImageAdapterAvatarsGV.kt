package com.example.memorygame.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.memorygame.R

class ImageAdapterAvatarsGV(private val items: MutableList<Int?>) : BaseAdapter() {

    // Retorna la cantidad de elementos
    override fun getCount(): Int {
        return items.size
    }

    // Retorna el item en la posición indicada
    override fun getItem(position: Int): Any {
        return items[position]!!
    }

    // Retorna el ID del item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Crea y configura la vista para cada item
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            // Infla el layout para el avatar
            val inflater = LayoutInflater.from(parent?.context)
            view = inflater.inflate(R.layout.avatar_icon_layout, parent, false)
        }

        // Obtiene el ImageView
        val imageView: ImageView = view?.findViewById(R.id.imageView) ?: return view!!

        // Si el item es transparente, desactiva la vista
        if (items[position] == Color.TRANSPARENT) {
            imageView.isEnabled = false
            imageView.setOnClickListener(null)
            imageView.alpha = 0.1f
            imageView.setBackgroundColor(Color.TRANSPARENT)
            imageView.setImageResource(Color.TRANSPARENT)
        } else {
            // Asigna el recurso de la imagen
            imageView.setImageResource(items[position]!!)
        }

        return view
    }
}
