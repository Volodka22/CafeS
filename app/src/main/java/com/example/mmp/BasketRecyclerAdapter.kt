package com.example.mmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class BasketRecyclerAdapter(private val products: Array<Product>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<BasketRecyclerAdapter.MyViewHolder>() {



    inner class MyViewHolder internal constructor(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val cntView: TextView = view.findViewById(R.id.cnt)
        internal val priceView : TextView = view.findViewById(R.id.sum)
        internal val mnsView: Button = view.findViewById(R.id.minus)
        internal val plsView: Button = view.findViewById(R.id.pls)



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketRecyclerAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.basket_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val product = products[position]
        holder.nameView.text = product.name
        holder.cntView.text = MainActivity.ordProd[product].toString()
        holder.priceView.text = (product.price* MainActivity.ordProd[product]!!).toString().plus(" рублей")

        holder.plsView.setOnClickListener{
            MainActivity.ordProd[product] = MainActivity.ordProd[product]!! + 1


            holder.cntView.text = MainActivity.ordProd[product].toString()
            holder.priceView.text = (product.price* MainActivity.ordProd[product]!!).toString().plus(" рублей")
        }

        holder.mnsView.setOnClickListener{
            MainActivity.ordProd[product] = MainActivity.ordProd[product]!! - 1

            holder.cntView.text = MainActivity.ordProd[product].toString()
            holder.priceView.text = (product.price* MainActivity.ordProd[product]!!).toString().plus(" рублей")
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = products.size


}