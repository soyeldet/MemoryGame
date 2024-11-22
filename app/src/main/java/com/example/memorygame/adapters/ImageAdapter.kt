package com.example.memorygame.adapters

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.R
import android.widget.ImageView
import androidx.cardview.widget.CardView

class ImageAdapter(val items: MutableList<Int?>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageFront: ImageView = view.findViewById(R.id.imageFront)
        val imageBack: ImageView = view.findViewById(R.id.imageBack)
        val cardFront: CardView = view.findViewById(R.id.cardFront)
        val cardBack: CardView = view.findViewById(R.id.cardBack)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (item != null) {
            holder.imageFront.setImageResource(R.drawable.back)
            holder.cardBack.visibility = View.GONE
            holder.cardBack.findViewById<ImageView>(R.id.imageBack).setImageResource(item)
        } else {
            holder.cardBack.visibility = View.GONE
        }

        holder.cardFront.setOnClickListener {
            flipCard(holder)
        }
    }

    fun flipCard(holder: ViewHolder) {
        val context = holder.itemView.context

        holder.itemView.isClickable = false

        val setOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_out) as AnimatorSet
        val setIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_in) as AnimatorSet

        setOut.setTarget(holder.cardFront)
        setIn.setTarget(holder.cardBack)

        setOut.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                holder.cardFront.visibility = View.GONE
                holder.cardBack.visibility = View.VISIBLE
                setIn.start()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        setIn.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                holder.itemView.isClickable = true
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {
                holder.itemView.isClickable = true
            }

            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })



        setOut.start()
    }

    fun flipBackCard(holder: ViewHolder) {
        val context = holder.itemView.context
        val setOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_out) as AnimatorSet
        val setIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_in) as AnimatorSet

        holder.cardBack.visibility = View.VISIBLE
        holder.cardFront.visibility = View.VISIBLE

        setOut.setTarget(holder.cardBack)
        setIn.setTarget(holder.cardFront)

        setOut.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                holder.cardBack.visibility = View.GONE
                holder.cardFront.visibility = View.VISIBLE
                setIn.start()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

        setOut.start()
    }

    override fun getItemCount() = items.size

    fun remove(index: Int) {
        if (index in items.indices) {
            items[index] = null
            notifyItemChanged(index)
        }
    }
}
