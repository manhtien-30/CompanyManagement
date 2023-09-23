package com.example.companymanagement.viewcontroller.fragment.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.companymanagement.R
import com.example.companymanagement.utils.DateParser
import com.example.companymanagement.utils.DateParser.Companion.toHumanReadDate
import com.example.companymanagement.utils.UtilsFuntion
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UserInfo : Fragment() {
    private var isEdit: Boolean = false
    private val user = FirebaseAuth.getInstance().currentUser
    private val cloudstore = FirebaseStorage.getInstance()
    private lateinit var infomodel: UserInfoViewModel
    private val imagepicked: MutableLiveData<Uri> = MutableLiveData(null)
    private lateinit var register: ActivityResultLauncher<Intent>
    private var isNewAvatar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infomodel = ViewModelProvider(this.requireActivity()).get(UserInfoViewModel::class.java)
        // register Image getter Activity

        register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.data?.data is Uri) {
                imagepicked.postValue(it.data?.data)
                isNewAvatar = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // declare id
        val name = view.findViewById<TextView>(R.id.userinfo_name)
        val phone = view.findViewById<TextView>(R.id.userinfo_phonenumber)
        val cemail = view.findViewById<TextView>(R.id.userinfo_email_contact)
        val pos = view.findViewById<TextView>(R.id.userinfo_position)
        val birthday = view.findViewById<TextView>(R.id.userinfo_birthdate)
        val gender = view.findViewById<TextView>(R.id.userinfo_gender)
        val cardid = view.findViewById<TextView>(R.id.userinfo_cardid)
        val cardidprovidedate = view.findViewById<TextView>(R.id.userinfo_card_id_create_date)
        val cardidprovideplace = view.findViewById<TextView>(R.id.userinfo_card_id_create_place)
        val uploadavatar = view.findViewById<ShapeableImageView>(R.id.avatar_change)
        val password = view.findViewById<TextInputEditText>(R.id.password_new)
        val passwordretype = view.findViewById<TextInputEditText>(R.id.password_new_retry)
        if (infomodel.info.value?.AvatarURL != null)
            imagepicked.postValue(Uri.parse(infomodel.info.value?.AvatarURL))
        uploadavatar.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            register.launch(i)
        }
        //two way binding
        fun applyEdit(isEdit: Boolean) {
            name.isEnabled = isEdit
            cemail.isEnabled = isEdit
            phone.isEnabled = isEdit
//            pos.isEnabled = isEdit
            birthday.isEnabled = isEdit
            gender.isEnabled = isEdit
//            cardid.isEnabled = isEdit
//            cardidprovidedate.isEnabled = isEdit
//            cardidprovideplace.isEnabled = isEdit
        }
        applyEdit(false)
        imagepicked.observe(this.viewLifecycleOwner) {
            val dp = UtilsFuntion.convertDPToPX(150.0F, context).toInt()
            Glide.with(this).load(imagepicked.value)
                .override(dp, dp)
                .centerCrop()
                .placeholder(CircularProgressDrawable(requireContext()).apply { start() })
                .error(R.drawable.ic_default_avatar)
                .into(uploadavatar)
        }
        infomodel.info.observe(viewLifecycleOwner) {
            if (it != null) {
                name.text = it.Name
                cemail.text = it.Email
                phone.text = it.PhoneNumber
                pos.text = it.Position
                birthday.text = it.BirthDate?.toHumanReadDate()
                gender.text = it.Gender
                cardid.text = it.IDCardNumber
                cardidprovidedate.text = it.IDCardCreateDate?.toHumanReadDate()
                cardidprovideplace.text = it.IDCardCreateLocation
            }
        }
        view.findViewById<Button>(R.id.avatar_accept).setOnClickListener {
            uploadImage()
        }
        view.findViewById<Button>(R.id.password_change_btn).setOnClickListener {

            if (password.text.toString() == passwordretype.text.toString()) {
                updateNewPassword(passwordretype.text.toString())
                password.text?.clear()
                passwordretype.text?.clear()
            } else
                toast("password is not match")
        }
        view.findViewById<FloatingActionButton>(R.id.infouser_btn_update)
            .setOnClickListener {
                isEdit = !isEdit
                applyEdit(isEdit)
                if (!isEdit) {
                    try {
                        infomodel.info.value = infomodel.info.value?.apply {
                            Name = name.text.toString()
                            PhoneNumber = phone.text.toString()
                            Email = cemail.text.toString()
                            Position = pos.text.toString()
                            BirthDate = DateParser.parser(birthday.text.toString())
                            Gender = gender.text.toString()
                            IDCardNumber = cardid.text.toString()
                            IDCardCreateDate =
                                DateParser.parser(cardidprovidedate.text.toString())
                            IDCardCreateLocation = cardid.text.toString()
                        }
                        toast("Chỉnh sữa thông tin thành công!!!")
                    } catch (ex: Exception) {
                        toast(ex.message!!)
                    }
                }
            }
    }

    private fun uploadImage() {
        Log.d("Avatar", imagepicked.value.toString() + "\n" + infomodel.info.value?.AvatarURL)
        val avatarURL = infomodel.info.value?.AvatarURL
        if (isNewAvatar) {
            val uuid = UUID.randomUUID()
            val ref = cloudstore.reference.child("public/avatar/${user?.uid}/av_$uuid")
            ref.putFile(imagepicked.value!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.storage?.downloadUrl?.addOnCompleteListener { uri ->
                        infomodel.info.value = infomodel.info.value?.apply {
                            this.AvatarURL = uri.result.toString()
                        }
                        toast("update Image Success")
                    }
                }
            }
            if (avatarURL?.isNullOrBlank() == true)
                cloudstore.getReferenceFromUrl(avatarURL)
                    .delete() // delete old image
                    .addOnCompleteListener {
                        toast("old image" + it.exception?.message!!)
                    }
        }
    }

    private fun updateNewPassword(newpassword: String) {
        user?.updatePassword(newpassword)?.addOnCompleteListener {
            if (it.isSuccessful)
                toast("Password Change Success")
            else {
                toast("Password Change" + it.exception.toString())
            }
        }
    }

    fun toast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}