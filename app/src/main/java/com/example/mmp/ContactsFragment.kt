package com.example.mmp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_contacts.*


class ContactsFragment : Fragment() {

    private val RC_SIGN_IN = 123


    private fun signIn(){
        sign.text = "Выйти"
        val params = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.NO_GRAVITY
        button.layoutParams = params

        sign.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

        Picasso.get().load(FirebaseAuth.getInstance().currentUser!!.photoUrl).
            resize(context!!.resources.displayMetrics.widthPixels,
                context!!.resources.displayMetrics.widthPixels).into(avatar)

        container_sign_in.visibility = View.GONE
        container_sign_out.visibility = View.VISIBLE


    }

    private fun signOut(){
        sign.text = "Войти"
        val params = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        button.layoutParams = params

        sign.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT

        container_sign_in.visibility = View.VISIBLE
        container_sign_out.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            signIn()
        } else {
            signOut()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                activity?.applicationContext?.let { it1 ->
                    AuthUI.getInstance().signOut(it1).addOnCompleteListener {
                        signOut()
                    }
                }

            } else {

                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            listOf(
                                AuthUI.IdpConfig.GoogleBuilder().build()
                            )
                        )
                        .setTheme(R.style.AppTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                    RC_SIGN_IN
                )
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                signIn()
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login", "Login canceled by User")
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    Log.e("Login", "No Internet Connection")
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error")
                    return
                }
            }
            Log.e("Login", "Unknown sign in response")
        }
    }

}