package com.example.mmp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_contacts.*


class ContactsFragment : Fragment(){

    private val RC_SIGN_IN = 123



    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            sign.text = "Выйти"
        }else{
            sign.text = "Войти"
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
            if (FirebaseAuth.getInstance().currentUser != null){
                activity?.applicationContext?.let {
                        it1 -> AuthUI.getInstance().signOut(it1).addOnCompleteListener{
                            sign.text = "Войти"
                        }
                }

            }else{

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
                sign.text = "Выйти"
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