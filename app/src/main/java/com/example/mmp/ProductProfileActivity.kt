package com.example.mmp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_profile.*
import kotlinx.android.synthetic.main.activity_product_profile.view.*
import kotlin.random.Random


class ProductProfileActivity : AppCompatActivity() {

    private lateinit var cafe: Cafe
    private lateinit var product: Product
    private var cnt = 0
    private var isLike = false
    private var isDislike = false
    private var likeCounter = 0
    private var dislikeCounter = 0

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_profile)

        init()
        getAuth()
        commentsDatabase()

        likeDislikeDatabase()


    }

    lateinit var likeRef:DatabaseReference
    lateinit var dislikeRef:DatabaseReference
    lateinit var likeCntRef:DatabaseReference
    lateinit var dislikeCntRef:DatabaseReference

    private fun likeDislikeDatabase() {

        likeCntRef = FirebaseDatabase.getInstance().reference.child(cafe.name).
            child("продукты").child(product.name).child("likes")

        dislikeCntRef = FirebaseDatabase.getInstance().reference.child(cafe.name).
            child("продукты").child(product.name).child("dislikes")

        getLikeCnt()
        getDislikeCnt()

        if (FirebaseAuth.getInstance().currentUser != null) {
            likeRef = FirebaseDatabase.getInstance().reference.child("Пользователи").
                child(getEmail(FirebaseAuth.getInstance().currentUser!!.email.toString())).
                child(cafe.name).child(product.name).child("like")

            dislikeRef = FirebaseDatabase.getInstance().reference.child("Пользователи").
                child(getEmail(FirebaseAuth.getInstance().currentUser!!.email.toString())).
                child(cafe.name).child(product.name).child("dislike")

            getIsLike()
            getIsDislike()
            likeListener()
            dislikeListener()
        }
    }


    private fun likeListener() {
        like.setOnClickListener {
            if (isLike) {
                likeRef.setValue(false)
                likeCntRef.setValue(likeCounter-1)
            } else {
                likeRef.setValue(true)
                likeCntRef.setValue(likeCounter+1)
                if(isDislike) {
                    dislikeRef.setValue(false)
                    dislikeCntRef.setValue(dislikeCounter-1)
                }
            }
        }
    }

    private fun dislikeListener() {
        dislike.setOnClickListener {
            if (isDislike) {
                dislikeRef.setValue(false)
                dislikeCntRef.setValue(dislikeCounter-1)
            } else {
                dislikeRef.setValue(true)
                dislikeCntRef.setValue(dislikeCounter+1)
                if(isLike) {
                    likeRef.setValue(false)
                    likeCntRef.setValue(likeCounter-1)
                }
            }
        }
    }

    private fun getIsDislike() {
        dislikeRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue(Boolean::class.java) == null) {
                    dislikeRef.setValue(false)
                } else {
                    isDislike = p0.getValue(Boolean::class.java)!!
                    val d = if (isDislike) R.drawable.ic_dislike_choose
                    else R.drawable.ic_dislike
                    dislike.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, d, null)
                    )
                }
            }

        })
    }

    private fun getIsLike() {
        likeRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue(Boolean::class.java) == null) {
                    likeRef.setValue(false)
                } else {
                    isLike = p0.getValue(Boolean::class.java)!!
                    val d = if (isLike) R.drawable.ic_like_choose
                    else R.drawable.ic_like
                    like.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, d, null)
                    )
                }
            }

        })
    }

    private fun getLikeCnt() {
        likeCntRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue(Int::class.java) == null) {
                    likeCntRef.setValue(0)
                } else {
                    likeCounter = p0.getValue(Int::class.java)!!
                    likeCnt.text = likeCounter.toString()
                }
            }

        })
    }

    private fun getDislikeCnt() {
        dislikeCntRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue(Int::class.java) == null) {
                    Log.e("anime","anime")
                    dislikeCntRef.setValue(0)
                } else {
                    dislikeCounter = p0.getValue(Int::class.java)!!
                    dislikeCnt.text = dislikeCounter.toString()
                }
            }

        })
    }

    private fun commentsDatabase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(cafe.name).child("комментарии").child(product.name)

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                (viewAdapter as CommentsAdapter).add(dataSnapshot.getValue<Comment>(Comment::class.java)!!)
                commentsEmpty.visibility = View.GONE
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
    }

    private fun getAuth() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(cafe.name).child("комментарии").child(product.name)
        if (FirebaseAuth.getInstance().currentUser == null) {
            textField.error = getString(R.string.Signin_befor_write)
            textField.input.isEnabled = false
        } else {
            textField.setEndIconOnClickListener {
                var txt = String()
                textField.editText!!.text.forEach {
                    if (!(it == '\n' && (txt.isEmpty() || txt[txt.length - 1] == '\n')))
                        txt += it.toString()
                }
                if (txt.isNotEmpty()) {
                    val comment = Comment()
                    comment.img = FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()
                    comment.name = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
                    comment.text = txt
                    myRef.child(myRef.push().key.toString()).setValue(comment)
                    input.setText("")
                }
            }
        }
    }

    private fun init() {
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
    }

    private fun getEmail(email:String):String{
        var a = ""
        email.forEach {
            if(it == '.') a += "()"
            else a += it
        }
        return a
    }

    override fun onBackPressed() {
        val int = Intent()
        int.putExtra("product", product)
        int.putExtra("cnt", cnt)
        setResult(Activity.RESULT_OK, int)
        finish()
    }

}