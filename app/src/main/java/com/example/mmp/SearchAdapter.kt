package com.example.mmp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class SearchAdapter(
    private val copyCafes: Array<Cafe>,
    private val adapterOnClick: (Cafe) -> Unit
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    private var cafes = mutableListOf(*copyCafes)


    inner class MyViewHolder internal constructor(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val addressView: TextView = view.findViewById(R.id.address)
        internal val icView: ImageView = view.findViewById(R.id.img)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cafe = cafes[position]
        Picasso.get().load(cafe.img).into(holder.icView)
        holder.nameView.text = cafe.name
        holder.addressView.text = cafe.address
        holder.itemView.setOnClickListener {
            adapterOnClick(cafe)
        }
    }

    fun filter(text: String) {
        cafes.clear()
        if (text.isEmpty()) {
            cafes = copyCafes.toMutableList()
        } else {
            copyCafes.forEach {
                if (it.name.toLowerCase().contains(text.toLowerCase()))
                    cafes.add(it)
            }
        }
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = cafes.size
}