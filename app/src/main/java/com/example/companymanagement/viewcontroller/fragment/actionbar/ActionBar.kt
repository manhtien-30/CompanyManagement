package com.example.companymanagement.viewcontroller.fragment.actionbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.companymanagement.R
import com.example.companymanagement.utils.UtilsFuntion
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import com.example.companymanagement.viewcontroller.fragment.user.UserManagerBottomSheet
import com.google.android.material.imageview.ShapeableImageView


class ActionBar : Fragment() {
    lateinit var infomodel: UserInfoViewModel;
    var bts = UserManagerBottomSheet.Instance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infomodel = ViewModelProvider(this.requireActivity()).get(UserInfoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_action_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var avatar = view.findViewById<ShapeableImageView>(R.id.action_bar_avatar)
        var displayname = view.findViewById<TextView>(R.id.action_bar_display_name)
        var email = view.findViewById<TextView>(R.id.action_bar_email_address)
        var userlayout = view.findViewById<ConstraintLayout>(R.id.action_bar_user_layout)
        infomodel.info.observe(viewLifecycleOwner) {
            if (it != null) {
                val dp = UtilsFuntion.convertDPToPX(32.0F, context).toInt()
                Glide.with(this)
                    .load(it.AvatarURL)
                    .override(dp, dp)
                    .centerCrop()
                    .placeholder(CircularProgressDrawable(requireContext()).apply { start() })
                    .error(R.drawable.ic_default_avatar)
                    .into(avatar)
                displayname.setText(it.Name)
                email.setText(it.Email)
            }
        }
        userlayout.setOnClickListener { e ->
            if (bts.isAdded == false) {
                bts.show(this.childFragmentManager, "userInfo");
            }
        }
    }

    fun show() {
        if (bts.isAdded == false) {

            bts.showNow(this.childFragmentManager, "userInfo");
        }
    }

}