package com.example.mmp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class PageRecyclerAdapter(private val products: Array<Product>, private val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<PageRecyclerAdapter.MyViewHolder>() {



    inner class MyViewHolder internal constructor(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val sptView: TextView = view.findViewById(R.id.supporting_text)
        internal val btnView: Button = view.findViewById(R.id.btn)
        internal val ic: ImageView = view.findViewById(R.id.ic)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageRecyclerAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.product_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val product = products[position]
        holder.nameView.text = product.name
        holder.sptView.text = product.shortText
        holder.btnView.text = product.price.toString().plus(" рублей")


        holder.btnView.setOnClickListener{
            if(MainActivity.ordProd[product] == null){
                MainActivity.ordProd[product] = 1
            }else MainActivity.ordProd[product] = MainActivity.ordProd[product]!! + 1

            MainActivity.badge.number++
            MainActivity.badge.isVisible = true

            //holder.ic.startAnimation(AnimationUtils.loadAnimation(context,R.anim.icon_anim))

        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = products.size
}