package com.example.mmp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private fun makeDialog(){

        val singleItems = arrayOf("Стол № 1", "Стол № 2", "Стол № 3","Стол № 4","Стол № 5","Стол № 6", "Стол № 7", "Стол № 8","Стол № 9")
        val checkedItem = 0

        var chooseItem = 1

        MaterialAlertDialogBuilder(activity)
            .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                // Respond to item chosen
                chooseItem = which+1
            }
            .setTitle(R.string.dialog_accept)
            .setPositiveButton(R.string.fire,
                DialogInterface.OnClickListener { _, _ ->
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("nightclub_user")

                    val user = User()

                    user.numberTable = chooseItem

                    MainActivity.ordProd.forEach {(key, cnt) ->
                        user.array.add(Pair(key.name,cnt))
                    }
                    user.id = Random.nextInt(1000000000)
                    myRef.child(user.id.toString()).setValue(user)
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { _, _ ->
                    // User cancelled the dialog
                }).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn.setOnClickListener {

            makeDialog()

        }

        ok = true

        upd()

    }

}