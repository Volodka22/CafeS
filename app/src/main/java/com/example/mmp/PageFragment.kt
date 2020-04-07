package com.example.mmp

import android.widget.TextView
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_page.*


class PageFragment : Fragment() {
    private val ARG_OBJECT = "object"

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var viewAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            viewManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            viewAdapter = PageRecyclerAdapter(MainActivity.product)

            recyclerView =  view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.pageRecyclerView).apply {

                setHasFixedSize(true)

                layoutManager = viewManager

                adapter = viewAdapter

            }
        }


    }
}