package com.example.mmp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlin.random.Random


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

        viewAdapter = BasketRecyclerAdapter(a.toTypedArray(),container1,container2,btn)

        recyclerView =  view!!.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.BasketRecyclerView).apply {

            setHasFixedSize(false)

            layoutManager = viewManager

            adapter = viewAdapter

        }
    }


    override fun onStart() {

        if(ok){
            if(MainActivity.ordProd.isEmpty()){
                container1.visibility = View.VISIBLE
                container2.visibility = View.GONE
            }else{
                container1.visibility = View.GONE
                container2.visibility = View.VISIBLE
            }
            upd()
        }
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("nightclub_user")

            val user = User()

            MainActivity.ordProd.forEach {(key, cnt) ->
                user.array.add(Pair(key.name,cnt))
            }
            user.id = Random.nextInt(1000000000)
            myRef.child(user.id.toString()).setValue(user)

        }

        ok = true

        upd()

    }
}