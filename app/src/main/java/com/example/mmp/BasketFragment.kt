package com.example.mmp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_basket.*


class BasketFragment : Fragment() {

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

    private fun upd() {
        val a = mutableListOf<Product>()

        MainActivity.ordProd.forEach { (key, _) ->
            a.add(MainActivity.product[key])
        }

        viewManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        viewAdapter = BasketRecyclerAdapter(a.toTypedArray(), container1, container2, btn)

        recyclerView =
            view!!.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.BasketRecyclerView)
                .apply {

                    setHasFixedSize(false)

                    layoutManager = viewManager

                    adapter = viewAdapter

                }
    }


    override fun onStart() {

        if (ok) {
            if (MainActivity.ordProd.isEmpty()) {
                container1.visibility = View.VISIBLE
                container2.visibility = View.GONE
            } else {
                container1.visibility = View.GONE
                container2.visibility = View.VISIBLE
            }
            upd()
        }
        super.onStart()
    }

    private fun makeDialog() {

        val singleItems = mutableListOf<String>()

        for(i in 1..MainActivity.cafe.countTable)
            singleItems.add("Стол № $i")

        val checkedItem = 0

        var chooseItem = 1


        MaterialAlertDialogBuilder(activity!!)
            .setSingleChoiceItems(singleItems.toTypedArray(), checkedItem) { _, which ->
                // Respond to item chosen
                chooseItem = which + 1
            }
            .setTitle(R.string.dialog_accept)
            .setPositiveButton(
                R.string.fire
            ) { _, _ ->
                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference.
                    child(MainActivity.cafe.name).child("заказы")

                val user = User()

                user.numberTable = chooseItem

                MainActivity.ordProd.forEach { (key, cnt) ->
                    user.array.add(User.Arr(MainActivity.product[key].name, cnt))
                }
                myRef.child(myRef.push().key!!).setValue(user)


                MainActivity.ordProd.clear()
                MainActivity.badge.number = 0
                MainActivity.badge.isVisible = false
                //TODO: приподнять snackbar
                Snackbar.make(recyclerView,"Заказ принят",Snackbar.LENGTH_LONG).show()
                container1.visibility = View.VISIBLE
                container2.visibility = View.GONE
                upd()

            }
            .setNegativeButton(
                R.string.cancel
            ) { _, _ ->
                // User cancelled the dialog
            }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn.setOnClickListener {

            makeDialog()

        }

        ok = true

        upd()

    }

}