package com.example.mmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class BasketRecyclerAdapter(products: Array<Product>,
                            private  val  shock:ImageView,
                            private  val  ops:TextView,
                            private  val rec : androidx.recyclerview.widget.RecyclerView) :
    androidx.recyclerview.widget.RecyclerView.Adapter<BasketRecyclerAdapter.MyViewHolder>() {

    private val myData = mutableListOf(*products)

    inner class MyViewHolder internal constructor(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val cntView: TextView = view.findViewById(R.id.cnt)
        internal val priceView : TextView = view.findViewById(R.id.sum)
        internal val mnsView: TextView = view.findViewById(R.id.minus)
        internal val plsView: TextView = view.findViewById(R.id.pls)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.basket_card, parent, false)
        return MyViewHolder(view)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val product = myData[position]
        holder.nameView.text = product.name
        holder.cntView.text = MainActivity.ordProd[product].toString()
        holder.priceView.text = (product.price * MainActivity.ordProd[product]!!).toString().plus("  ₽")

        holder.plsView.text = "+"

        holder.plsView.setOnClickListener{
            MainActivity.ordProd[product] = MainActivity.ordProd[product]!! + 1


            MainActivity.badge.number++



            holder.cntView.text = MainActivity.ordProd[product].toString()
            holder.priceView.text = (product.price* MainActivity.ordProd[product]!!).toString().plus("  ₽")
        }


        holder.mnsView.setOnClickListener{

            if(MainActivity.ordProd[product] != null) {

                MainActivity.ordProd[product] = MainActivity.ordProd[product]!! - 1

                MainActivity.badge.number--


                if (MainActivity.ordProd[product] == 0) {
                    MainActivity.ordProd.remove(product)
                    myData.remove(product)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, myData.size)


                    if (MainActivity.badge.number == 0) {
                        MainActivity.badge.isVisible = false
                        rec.visibility = View.GONE
                        shock.visibility = View.VISIBLE
                        ops.visibility = View.VISIBLE
                    }


                } else {
                    holder.cntView.text = MainActivity.ordProd[product].toString()
                    holder.priceView.text =
                        (product.price * MainActivity.ordProd[product]!!).toString().plus("  ₽")
                }
            }
        }





    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myData.size


}