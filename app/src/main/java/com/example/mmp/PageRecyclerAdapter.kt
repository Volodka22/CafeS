package com.example.mmp

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.product_card.view.*

class PageRecyclerAdapter(private val products: Array<Product>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<PageRecyclerAdapter.MyViewHolder>() {



    inner class MyViewHolder internal constructor(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val sptView: TextView = view.findViewById(R.id.supporting_text)
        internal val btnView: Button = view.findViewById(R.id.btn)

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

        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = products.size
}