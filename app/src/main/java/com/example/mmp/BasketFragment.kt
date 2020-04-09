package com.example.mmp

import android.content.Context
import android.os.Bundle
import android.util.Log
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

    private var prod =  emptyArray<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onStart() {
        if(ok){

            val a = mutableListOf<Product>()

            MainActivity.ordProd.forEach{ (key, _) ->
                a.add(key)
            }

            viewManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

            viewAdapter = BasketRecyclerAdapter(a.toTypedArray())

            recyclerView =  view!!.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.BasketRecyclerView).apply {

                setHasFixedSize(false)

                layoutManager = viewManager

                adapter = viewAdapter

            }
        }
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ok = true


        val a = mutableListOf<Product>()

        MainActivity.ordProd.forEach{ (key, _) ->
            a.add(key)
        }

        viewManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        viewAdapter = BasketRecyclerAdapter(a.toTypedArray())

        recyclerView =  view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.BasketRecyclerView).apply {

            setHasFixedSize(false)

            layoutManager = viewManager

            adapter = viewAdapter

        }



    }
}