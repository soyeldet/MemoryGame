package com.example.memorygame.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.memorygame.R

class ImageAdapterGridView(private val items: MutableList<Int?>, private val items1: MutableList<Int?>) : BaseAdapter() {

    // Lista para almacenar las respuestas correctas
    val correctAnswer = mutableListOf<Int>()

    // Retorna el número de items en el GridView
    override fun getCount(): Int {
        return items.size
    }

    // Configura la vista para cada item del GridView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val gridView: View
        val imageView: ImageView

        // Si la vista no está reciclada, infla la nueva vista
        if (convertView == null) {
            val inflater = LayoutInflater.from(parent?.context)
            gridView = inflater.inflate(R.layout.grid_item_layout, parent, false)
            imageView = gridView.findViewById(R.id.imageView)
            gridView.tag = imageView
        } else {
            // Usa la vista reciclada
            gridView = convertView
            imageView = gridView.tag as ImageView
        }

        // Asigna la imagen al ImageView
        items[position]?.let { imageView.setImageResource(it) }
        return gridView
    }

    // Retorna el item en la posición indicada
    override fun getItem(position: Int): Int? {
        return items[position]
    }

    // Retorna el ID del item (en este caso, la posición)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Añade un item a la lista de respuestas correctas
    fun addItem(item: Int) {
        // Asegura que no se sobrepasen los límites
        if (correctAnswer.size < items.size) {
            // Agrega el item a la lista de respuestas correctas
            items[correctAnswer.size] = item
            correctAnswer.add(item)
            // Notifica que los datos del adapter han cambiado
            notifyDataSetChanged()
        }
    }
}
