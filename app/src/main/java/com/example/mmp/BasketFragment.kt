package com.example.mmp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_basket.*


class BasketFragment : Fragment(){

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var viewAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    private var ok = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    private fun upd(){
        val a = mutableListOf<Product>()

        MainActivity.ordProd.forEach{ (key, _) ->
            a.add(key)
        }

        viewManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        viewAdapter = BasketRecyclerAdapter(a.toTypedArray(),shock,ops,BasketRecyclerView)

        recyclerView =  view!!.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.BasketRecyclerView).apply {

            setHasFixedSize(false)

            layoutManager = viewManager

            adapter = viewAdapter

        }
    }


    override fun onStart() {

        if(ok){
            if(MainActivity.ordProd.isEmpty()){
                shock.visibility = View.VISIBLE
                ops.visibility = View.VISIBLE
                BasketRecyclerView.visibility = View.GONE
            }else{
                shock.visibility = View.GONE
                ops.visibility = View.GONE
                BasketRecyclerView.visibility = View.VISIBLE
            }
            upd()
        }
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ok = true

        upd()

    }
}