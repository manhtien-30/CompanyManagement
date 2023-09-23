package com.example.companymanagement.viewcontroller.activity.main

import OnSwipeTouchListener
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.R
import com.example.companymanagement.utils.Dimension
import com.example.companymanagement.viewcontroller.activity.login.LoginActivity
import com.example.companymanagement.viewcontroller.fragment.actionbar.ActionBar
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserRoleViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.companymanagement.utils.UtilsFuntion

class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var infomodel: UserInfoViewModel;
    lateinit var rolemodel: UserRoleViewModel;

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infomodel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        rolemodel = ViewModelProvider(this).get(UserRoleViewModel::class.java)

        if (auth.currentUser == null) {
            goBackLogin()
        }
        auth.addAuthStateListener {
            Log.d("User", it.currentUser.toString());
            if (it.currentUser == null) {
                goBackLogin()
            } else {
                infomodel.retriveUseInfo(it.currentUser!!.uid)
                rolemodel.getRole(it.currentUser!!.uid)
            }
        }
        supportActionBar?.hide();
        setContentView(R.layout.activity_main)
        // gesture topbar
        var actionbar = supportFragmentManager.findFragmentById(R.id.action_bar) as ActionBar
//        var containerfragment =
//            findViewById<FrameLayout>(R.id.activity_container)
//        containerfragment.setOnClickListener {
//            Log.d("click", "end!!!")
//        }

//        containerfragment.setOnTouchListener(object :
//            OnSwipeTouchListener(applicationContext,
//                UtilsFuntion.convertVHToPX(0.1F, Dimension.width, applicationContext),
//                UtilsFuntion.convertVHToPX(0.3F, Dimension.height, applicationContext)) {
//            override fun onSwipeUp() {
//                actionbar.show()
//                Log.d("swipe", "up")
//            }
//
//        })
    }

    fun goBackLogin() {
        val usernull = Intent(this, LoginActivity::class.java)
        startActivity(usernull)
        this.finish()
    }
}