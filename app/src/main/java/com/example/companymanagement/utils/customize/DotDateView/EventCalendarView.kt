package com.example.companymanagement.utils.customize.DotDateView

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import com.example.companymanagement.R
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*
import com.example.companymanagement.databinding.CalendarHeaderBinding

class EventCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {
    //listener


    internal var dayselectchange: OnDateChangeListener? = null;

    internal var onmonthchange: onMonthChangeListener? = null;
    private var oneventchange: onEventChangeListener? = null;

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    internal val calendarview: CalendarView;

    private var daybinder: com.example.companymanagement.utils.customize.DotDateView.DayBinder
    fun setDaySelectChangeListener(e: OnDateChangeListener) {
        dayselectchange = e;
    }

    fun setOnMonthChangeListener(e: onMonthChangeListener) {
        onmonthchange = e;
    }


    init {

        var view = inflate(context, R.layout.customview_event_calender, this)
        calendarview = view.findViewById(R.id.calendarView)
        daybinder = DayBinder(this)
        calendarview.dayBinder = daybinder
        calendarview.monthHeaderBinder = MonthHeaderFooterBinder()
        bind();
    }

    fun addAllEvent(arr: Iterable<DateEvent>) {
        for (e in arr) {
            daybinder.EventList[e.pair.first] = e.pair.second
            calendarview.notifyDateChanged(e.pair.first)
        }

        oneventchange?.onChange(arr, DateEventChangeState.ADD, this)
    }

    fun addEvent(e: DateEvent) {
        daybinder.EventList[e.pair.first] = e.pair.second
        calendarview.notifyDateChanged(e.pair.first)
        oneventchange?.onChange(listOf(e), DateEventChangeState.ADD, this);
    }

    fun removeEvent(e: LocalDate) {
        daybinder.EventList.remove(e)
        calendarview.notifyDateChanged(e)
        oneventchange?.onChange(null, DateEventChangeState.REMOVE, this);
    }

    fun clearEvent() {
        daybinder.EventList.clear()
        oneventchange?.onChange(null, DateEventChangeState.CLEAR, this)
    }

    private fun bind() {
        calendarSetup()
        initMonthSwitchButton()
        initTitle()
    }

    private fun calendarSetup() {
        val currentMonth = YearMonth.now()
        val daysOfWeek = WeekFields.of(Locale.getDefault())
        calendarview.setup(currentMonth.minusMonths(50),
            currentMonth.plusMonths(50),
            daysOfWeek.firstDayOfWeek)
        calendarview.scrollToMonth(currentMonth)
    }

    fun initTitle() {
        var monthYearText = this.findViewById<TextView>(R.id.monthYearText)
        calendarview.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            monthYearText.text = title
        }
    }


    private fun initMonthSwitchButton() {
        var previou = this.findViewById<AppCompatImageView>(R.id.previousMonthImage)
        var next = this.findViewById<AppCompatImageView>(R.id.nextMonthImage)
        next.setOnClickListener {
            calendarview.findFirstVisibleMonth()?.let {
                this.onmonthchange?.onChange(it.yearMonth, it.yearMonth.next, this)

                calendarview.smoothScrollToMonth(it.yearMonth.next)

            }
        }

        previou.setOnClickListener {
            calendarview.findFirstVisibleMonth()?.let {
                this.onmonthchange?.onChange(it.yearMonth, it.yearMonth.previous, this)
                calendarview.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }


}

class DayBinder(val cal: EventCalendarView) : DayBinder<DayViewContainer> {

    internal var selectedDate: LocalDate = LocalDate.MIN;
    internal var EventList: MutableMap<LocalDate, Color> = mutableMapOf()

    override fun create(view: View) = DayViewContainer(view)

    override fun bind(container: DayViewContainer, day: CalendarDay) {
        container.day = day
        val textView = container.binding.calendarDayText
        textView.text = day.day.toString();
        if (EventList.containsKey(day.date)) {
            var value = EventList.get(day.date)
            if (value != null) {
                container.setDotColor(value)
            }
        } else {
            container.clearDotColor()
        }
        if (day.owner == DayOwner.THIS_MONTH) {
            textView.visibility = View.VISIBLE
            if (day.date == selectedDate) {
                textView.setTextColor(Color.WHITE)
                container.view.setBackgroundResource(R.drawable.selected_bg)
            } else {
                textView.setTextColor(Color.BLACK)
                container.view.background = null
            }
        }

        if (day.owner == DayOwner.THIS_MONTH) {
            textView.setTextColor(Color.BLUE)
        } else {
            textView.setTextColor(Color.BLACK)
        }
        container.view.setOnClickListener {

            if (selectedDate == day.date) {
                cal.calendarview.notifyDateChanged(selectedDate)
                selectedDate = LocalDate.MIN
                cal.dayselectchange?.onSelectedDayChange(cal.calendarview, selectedDate)
            } else
                if (day.owner == DayOwner.THIS_MONTH) {
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        cal.dayselectchange?.onSelectedDayChange(cal.calendarview, selectedDate)
                        Log.d("date", "change")
                        cal.calendarview.notifyDateChanged(day.date)
                        oldDate?.let {
                            cal.calendarview.notifyDateChanged(it)
                        }
                    }
                } else if (day.owner == DayOwner.PREVIOUS_MONTH || day.owner == DayOwner.NEXT_MONTH) {
                    cal.calendarview.notifyDateChanged(day.date, DayOwner.THIS_MONTH)
                    cal.calendarview.scrollToDate(day.date)
                    cal.onmonthchange?.onChange(YearMonth.from(
                        if (day.owner == DayOwner.PREVIOUS_MONTH) day.date.minusMonths(1)
                        else day.date.plusMonths(1)),
                        YearMonth.from(day.date), cal)
                    selectedDate = day.date
                }
        }

    }
}

fun interface OnDateChangeListener {
    fun onSelectedDayChange(
        view: CalendarView,
        localDate: LocalDate,
    )
}

enum class DateEventChangeState {
    ADD, CLEAR, REMOVE
}

fun interface onMonthChangeListener {
    fun onChange(oldmonth: YearMonth, newmonth: YearMonth, e: View)
}

fun interface onEventChangeListener {
    fun onChange(event: Iterable<DateEvent>?, state: DateEventChangeState, e: View)
}

data class DateEvent(val d: LocalDate, val c: Color) {
    val pair: Pair<LocalDate, Color>

    init {
        pair = Pair(d, c)
    }
}

class MonthHeaderFooterBinder :
    com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder<MonthViewContainer> {
    override fun create(view: View) = MonthViewContainer(view)
    override fun bind(container: MonthViewContainer, month: CalendarMonth) {
        // Setup each header day text if we have not done that already.

        if (container.legendLayout.tag == null) {
            container.legendLayout.tag = month.yearMonth
            container.legendLayout.children.map { it as TextView }
                .forEachIndexed { index, tv ->
                    tv.text = DayOfWeek.of(index + 1)
                        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        .toUpperCase(Locale.ENGLISH)
                }
        }
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay

    var binding = com.example.companymanagement.databinding.CalendarDayBinding.bind(view)
    var textview = binding.calendarDayText
    var dot = binding.calendarDayDot
    fun clearDotColor() {
        binding.calendarDayDot.visibility = View.GONE;
    }

    fun setDotColor(color: Color) {
        binding.calendarDayDot.visibility = View.VISIBLE;
        binding.calendarDayDot.setBackgroundColor(color.toArgb())
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    val legendLayout =
        CalendarHeaderBinding.bind(view).legendLayout
}
