package com.example.mmp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_profile.*
import kotlinx.android.synthetic.main.activity_product_profile.view.*
import kotlin.random.Random


class ProductProfileActivity : AppCompatActivity() {

    private lateinit var cafe: Cafe
    private lateinit var product: Product
    private var cnt = 0

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_profile)
        cafe = intent.getSerializableExtra("cafe") as Cafe
        product = intent.getSerializableExtra("product") as Product
        Picasso.get().load(product.img).resize(
            this.resources.displayMetrics.widthPixels,
            this.resources.displayMetrics.widthPixels
        ).into(backdrop)

        name.text = product.name
        info.text = product.shortText


        price.text = "${product.price} рублей"

        price.setOnClickListener {
            cnt++
            Snackbar.make(main, getString(R.string.add_product), Snackbar.LENGTH_LONG).show()
        }

        viewManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        viewAdapter = CommentsAdapter()

        recyclerView.apply {

            setHasFixedSize(false)

            layoutManager = viewManager

            adapter = viewAdapter
        }

        recyclerView.isNestedScrollingEnabled = false

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("${cafe.name}_${product.name}_comments")

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                (viewAdapter as CommentsAdapter).add(dataSnapshot.getValue<Comment>(Comment::class.java)!!)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        if (FirebaseAuth.getInstance().currentUser == null) {
            textField.error = getString(R.string.Signin_befor_write)
            textField.input.isEnabled = false
        }else{
            textField.setEndIconOnClickListener {
                var txt = String()
                textField.editText!!.text.forEach {
                    if(!(it == '\n' &&  (txt.isEmpty() || txt[txt.length-1] == '\n')))
                        txt += it.toString()
                }
                if(txt.isNotEmpty()){
                    val comment = Comment()
                    comment.id = Random.nextInt(1000000000)
                    comment.img = FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()
                    comment.name = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
                    comment.text = txt
                    myRef.child(comment.id.toString()).setValue(comment)
                    input.setText("")
                }
            }
        }




    }


    override fun onBackPressed() {
        val int = Intent()
        int.putExtra("product", product)
        int.putExtra("cnt", cnt)
        setResult(Activity.RESULT_OK, int)
        finish()
    }

}