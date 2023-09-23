package com.example.companymanagement.viewcontroller.fragment.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.activity.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPass : Fragment(){
    private var mAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        val sendmail = root.findViewById<Button>(R.id.b_send_mail)
        val mail = root.findViewById<EditText>(R.id.ed_mail)
        mAuth = FirebaseAuth.getInstance()
        sendmail.setOnClickListener{
            val email = mail.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(activity, "Enter your email!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this.activity,
                                "Check email to reset your password!",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this.activity,
                                "Fail to send reset password email!",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
        return root
    }
}