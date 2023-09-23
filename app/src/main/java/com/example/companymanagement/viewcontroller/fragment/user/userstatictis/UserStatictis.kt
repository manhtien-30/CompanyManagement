package com.example.companymanagement.viewcontroller.fragment.user.userstatictis

import android.graphics.Color
import android.graphics.Color.valueOf
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.companymanagement.R
import com.example.companymanagement.utils.DateParser
import com.example.companymanagement.utils.DateTimeExt.Companion.toLocalDate
import com.example.companymanagement.utils.customize.DotDateView.DateEvent
import com.example.companymanagement.utils.customize.DotDateView.EventCalendarView
import com.example.companymanagement.viewcontroller.fragment.user.CheckinViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class UserStatictis : Fragment() {

    val c = Calendar.getInstance().time

    val month = SimpleDateFormat("MM", Locale.getDefault())
    val year = SimpleDateFormat("yyyy", Locale.getDefault())

    var currentdayofwork: Int = 25;

    val user = FirebaseAuth.getInstance().currentUser
    private var checkingModel: CheckinViewModel = CheckinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_statictis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = view.findViewById<TextView>(R.id.textview_title)
        val work = view.findViewById<TextView>(R.id.datework)
        val late = view.findViewById<TextView>(R.id.datelate)
        val absent = view.findViewById<TextView>(R.id.dateabsent)

        title.text = "Thống kê Checkin tháng " + month.format(c).toString() + " / " + year.format(c)
            .toString()

        //-----------Calendar------------//

        val calendarView =
            view.findViewById<EventCalendarView>(R.id.statistics_calendar_view)

        var current = Date()
        var start = Date(current.year, current.month, 1)
        currentdayofwork = DateParser.getBusinessDay(start.toLocalDate(), current.toLocalDate());
        checkingModel.reTriveCheckinAll(this.user?.uid.toString(), start, current)
            .observe(viewLifecycleOwner) {
                work.text = "${it.size}/$currentdayofwork"
                var checklist = it.map {
                    DateEvent(it.checked_date.toLocalDate(), valueOf(Color.GREEN))
                }
                absent.text = (currentdayofwork - it.size).toString()
                var datepointer = start.toLocalDate()
                while (datepointer < current.toLocalDate()) {
                    calendarView.addEvent(DateEvent(datepointer, valueOf(Color.RED)))
                    datepointer = datepointer.plusDays(1)
                }
                calendarView.addAllEvent(checklist)
            }

        checkingModel.retriveLate(this.user?.uid.toString(), start, current)
            .observe(viewLifecycleOwner) {
                late.text = "${it.size}"
                var checklist = it.map {
                    DateEvent(it.checked_date.toLocalDate(), valueOf(Color.YELLOW))
                }
                calendarView.addAllEvent(checklist)
            }
    }

}