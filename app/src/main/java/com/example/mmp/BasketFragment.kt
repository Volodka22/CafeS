package com.example.mmp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
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

    private fun makeDialog(numberTable: Int) {

        if(numberTable == 0) return

        MaterialAlertDialogBuilder(activity!!)
            .setTitle("Ваш стол № $numberTable. Подтвердите заказ.")
            .setPositiveButton(
                R.string.fire
            ) { _, _ ->
                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference.
                    child(MainActivity.cafe.name).child("заказы")

                val user = User()

                user.numberTable = numberTable

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data != null){
            makeDialog(data.getIntExtra("numberTable",0))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn.setOnClickListener {

            startActivityForResult(Intent(activity,QrActivity::class.java),1)

        }

        ok = true

        upd()

    }



}