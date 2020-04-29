package com.example.mmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.util.*

class CommentsAdapter :
    androidx.recyclerview.widget.RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    private val comments = LinkedList<Comment>()


    inner class MyViewHolder internal constructor(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val addressView: TextView = view.findViewById(R.id.main_text)
        internal val icView: ImageView = view.findViewById(R.id.img)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = comments[position]
        Picasso.get().load(comment.img).into(holder.icView)
        holder.nameView.text = comment.name
        holder.addressView.text = comment.text
    }

    fun add(comment: Comment) {
        comments.addFirst(comment)
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = comments.size
}