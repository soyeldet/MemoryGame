package com.example.memorygame.adapters

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.R
import android.widget.ImageView
import androidx.cardview.widget.CardView

// Adaptador para manejar un RecyclerView con cartas que se pueden voltear
class ImageAdapterRecyclerVIew(val items: MutableList<Int?>) : RecyclerView.Adapter<ImageAdapterRecyclerVIew.ViewHolder>() {

    // Método para interceptar los eventos táctiles en el RecyclerView (en este caso no hace nada y siempre devuelve true)
    fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return true
    }

    // ViewHolder contiene las vistas que se usarán para cada ítem en el RecyclerView
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageFront: ImageView = view.findViewById(R.id.imageFront)  // Imagen del frente de la tarjeta
        val imageBack: ImageView = view.findViewById(R.id.imageBack)    // Imagen del reverso de la tarjeta
        val cardFront: CardView = view.findViewById(R.id.cardFront)      // CardView para el frente
        val cardBack: CardView = view.findViewById(R.id.cardBack)        // CardView para el reverso
    }

    // Este método infla el layout del ítem en el RecyclerView y devuelve un ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_layout, parent, false)
        return ViewHolder(view)
    }

    // Vincula los datos del ítem con las vistas del ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Si hay una imagen en el ítem, la muestra en la parte posterior de la tarjeta
        if (item != null) {
            holder.imageFront.setImageResource(R.drawable.back) // Muestra la parte de atrás por defecto
            holder.cardBack.visibility = View.GONE             // Oculta el reverso inicialmente
            holder.cardBack.findViewById<ImageView>(R.id.imageBack).setImageResource(item) // Coloca la imagen en el reverso
        } else {
            holder.cardBack.visibility = View.GONE // Si no hay imagen, también oculta el reverso
        }

        // Establece un listener para voltear la tarjeta cuando se toque el frente
        holder.cardFront.setOnClickListener {
            flipCard(holder)
        }
    }

    // Método para voltear la tarjeta (animación de flip)
    fun flipCard(holder: ViewHolder) {
        val context = holder.itemView.context

        holder.itemView.isClickable = false // Deshabilita la tarjeta para evitar clics repetidos durante la animación

        // Carga los animadores para voltear la tarjeta
        val setOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_out) as AnimatorSet
        val setIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_in) as AnimatorSet

        // Establece el objetivo de las animaciones
        setOut.setTarget(holder.cardFront)
        setIn.setTarget(holder.cardBack)

        // Listener para cuando la animación de "salir" termine
        setOut.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                holder.cardFront.visibility = View.GONE  // Oculta el frente de la tarjeta
                holder.cardBack.visibility = View.VISIBLE // Muestra el reverso de la tarjeta
                setIn.start()  // Comienza la animación para hacer visible el reverso
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        // Listener para cuando la animación de "entrar" termine
        setIn.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                holder.itemView.isClickable = true // Vuelve a habilitar el clic después de la animación
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {
                holder.itemView.isClickable = true
            }

            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        setOut.start() // Inicia la animación de salida
    }

    // Método para voltear la tarjeta de nuevo (volviendo al estado inicial)
    fun flipBackCard(holder: ViewHolder) {
        val context = holder.itemView.context
        val setOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_out) as AnimatorSet
        val setIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_in) as AnimatorSet

        holder.cardBack.visibility = View.VISIBLE // Asegura que el reverso sea visible
        holder.cardFront.visibility = View.VISIBLE // Asegura que el frente sea visible

        // Establece el objetivo de las animaciones
        setOut.setTarget(holder.cardBack)
        setIn.setTarget(holder.cardFront)

        // Listener para la animación de "salir"
        setOut.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                holder.cardBack.visibility = View.GONE  // Oculta el reverso
                holder.cardFront.visibility = View.VISIBLE // Muestra el frente
                setIn.start()  // Comienza la animación para hacer visible el frente
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        setOut.start() // Inicia la animación de salida
    }

    // Método que devuelve la cantidad de ítems en la lista
    override fun getItemCount() = items.size

    // Método para eliminar un ítem de la lista
    fun remove(index: Int) {
        if (index in items.indices) {
            items[index] = null  // Establece el valor en null para que la tarjeta no tenga imagen
            notifyItemChanged(index)  // Notifica al adaptador que el ítem ha cambiado
        }
    }
}
