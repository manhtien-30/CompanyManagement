package com.example.companymanagement.viewcontroller.fragment.qrscanner

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.companymanagement.R
import com.example.companymanagement.model.checkin.CheckinModel
import com.example.companymanagement.utils.DateTimeExt.Companion.toHumanDateAndTime
import com.example.companymanagement.utils.DateTimeExt.Companion.toHumanReadTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.BarcodeFormat
import io.github.g00fy2.quickie.config.ScannerConfig
import io.github.g00fy2.quickie.content.QRContent
import java.text.SimpleDateFormat
import java.util.*


class QRScanner : Fragment() {
    val store = FirebaseFirestore.getInstance();
    var qrcode: MutableLiveData<String> = MutableLiveData()
    var goback: MutableLiveData<Boolean> = MutableLiveData(false)
    val scanner = registerForActivityResult(ScanCustomCode()) {
        if (it is QRResult.QRSuccess && it.content is QRContent.Plain)
            qrcode.postValue(it.content.rawValue)
        else if (it is QRResult.QRUserCanceled) {
            goback.postValue(true)
        } else if (it is QRResult.QRMissingPermission) {
            toast("No camera permission")
            goback.postValue(true)
        }
    }
    val currentuser = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        launchScaner()
        return View(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrcode.observe(this.viewLifecycleOwner) {
            if (it != null) {
                try {
                    Log.d("QRcore", it)
                    var result = Base64.decode(it, Base64.NO_PADDING or Base64.NO_WRAP)
                    Log.d("QRcore", String(result))
                    var substring = String(result).split("|");
                    if (substring[0] == "checkin") {
                        var doc = String(result).split("|")[1];
                        store.collection("checkin").document(doc).get()
                            .addOnCompleteListener { doc ->
                                doc.result!!.reference.collection("/checked_user").whereEqualTo("owneruuid",currentuser?.uid!!).get().addOnSuccessListener{
                                    if(it.isEmpty){
                                        var day = Calendar.getInstance()
                                        var c = Calendar.getInstance().time
                                        val month = SimpleDateFormat("MM", Locale.getDefault())
                                        val year = SimpleDateFormat("yyyy", Locale.getDefault())
                                        val date = SimpleDateFormat("dd", Locale.getDefault())
                                        day.set(year.format(c).toInt(),month.format(c).toInt() - 1,date.format(c).toInt(),8,0,0)
                                        val check = CheckinModel()
                                        check.apply {
                                            this.owneruuid = currentuser?.uid!!
                                            if(checked_date?.before(day.time) == true){
                                                this.status = "ontime"
                                                Log.d("status",checked_date.toString())
                                                Log.d("status",day.time.toString())
                                                Log.d("status",checked_date?.before(day.time).toString())
                                            }
                                            else{
                                                this.status = "late"
                                            }
                                        }
                                        doc.result!!.reference.collection("/checked_user").add(check).addOnSuccessListener {
                                            toast("Checking thành công ${check.checked_date?.toHumanDateAndTime()}")
                                            goback.postValue(true)
                                        }
                                    }
                                    else{
                                        toast("Bạn đã checkin hôm nay rồi vào lúc ${
                                            it.documents[0].toObject(com.example.companymanagement.model.checkin.CheckinModel::class.java)?.checked_date?.toHumanReadTime()
                                        }")
                                        goback.postValue(true)
                                    }
                                }
                            }
                    } else {
                        throw IllegalArgumentException()
                    }
                } catch (e: IllegalArgumentException) {
                    toast("Không phải checkin QR ")
                    launchScaner()
                }
            }

        }
        goback.observe(viewLifecycleOwner) {
            if (it == true) {
                this.parentFragmentManager.popBackStack()
            }
        }

    }

    fun launchScaner() {
        scanner.launch(
            ScannerConfig.build {
                setBarcodeFormats(listOf(BarcodeFormat.FORMAT_QR_CODE))
                setOverlayStringRes(R.string.qrscaner_string)
                setHapticSuccessFeedback(true)
            },
        )
    }

    fun toast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show()
    }

}