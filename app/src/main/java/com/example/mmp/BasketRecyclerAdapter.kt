package com.example.mmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso


class BasketRecyclerAdapter(
    products: Array<Product>,
    private val container1: LinearLayout,
    private val container2: FrameLayout,
    private val btn: Button
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<BasketRecyclerAdapter.MyViewHolder>() {

    private val myData = mutableListOf(*products)
    private var sum = 0

    inner class MyViewHolder internal constructor(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val cntView: TextView = view.findViewById(R.id.cnt)
        internal val priceView: TextView = view.findViewById(R.id.sum)
        internal val mnsView: TextView = view.findViewById(R.id.minus)
        internal val plsView: TextView = view.findViewById(R.id.pls)
        internal val ic: ImageView = view.findViewById(R.id.img)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.basket_card, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val product = myData[position]
        val pos = MainActivity.product.indexOf(product)
        holder.nameView.text = product.name
        holder.cntView.text = MainActivity.ordProd[pos].toString()
        holder.priceView.text = (product.price * MainActivity.ordProd[pos]!!).toString().plus("  ₽")

        sum += product.price * MainActivity.ordProd[pos]!!

        btn.text = "ОФОРМИТЬ ЗАКАЗ ЗА $sum ₽"

        Picasso.get().load(product.img).into(holder.ic)

        holder.plsView.setOnClickListener {

            sum += product.price

            btn.text = "ОФОРМИТЬ ЗАКАЗ ЗА $sum ₽"

            MainActivity.ordProd[pos] = MainActivity.ordProd[pos]!! + 1


            MainActivity.badge.number++



            holder.cntView.text = MainActivity.ordProd[pos].toString()
            holder.priceView.text =
                (product.price * MainActivity.ordProd[pos]!!).toString().plus("  ₽")


        }


        holder.mnsView.setOnClickListener {

            sum -= product.price

            btn.text = "ОФОРМИТЬ ЗАКАЗ ЗА $sum ₽"

            if (MainActivity.ordProd[pos] != null) {

                MainActivity.ordProd[pos] = MainActivity.ordProd[pos]!! - 1

                MainActivity.badge.number--




                if (MainActivity.ordProd[pos] == 0) {
                    MainActivity.ordProd.remove(pos)
                    myData.remove(product)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, myData.size)


                    if (MainActivity.badge.number == 0) {
                        MainActivity.badge.isVisible = false
                        container1.visibility = View.VISIBLE
                        container2.visibility = View.GONE
                    }


                } else {
                    holder.cntView.text = MainActivity.ordProd[pos].toString()
                    holder.priceView.text =
                        (product.price * MainActivity.ordProd[pos]!!).toString().plus("  ₽")
                }
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myData.size


}