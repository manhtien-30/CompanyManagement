package com.example.companymanagement.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class DateParser {
    companion object {
        val simpleDateParser = SimpleDateFormat("dd/MM/yyyy")
        val simpleTimeParser = SimpleDateFormat("hh:mm:ss a")
        val simpleDateAndTimeParser = SimpleDateFormat("dd/MM/yyyy | hh:mm:ss a")
        fun Date.toHumanReadDate(): String {
            return simpleDateParser.format(this) // this here is date
        }


        fun parser(date: String): Date {
            var date = simpleDateParser.parse(date);
            if (date == null) {
                throw Exception("Date input error")
            } else return date;
        }

        fun getBusinessDay(from: LocalDate, to: LocalDate): Int {
            var totalDays = 0
            var date: LocalDate = from
            while (date < to) {
                if (date.dayOfWeek !== DayOfWeek.SATURDAY
                    && date.dayOfWeek !== DayOfWeek.SUNDAY
                )
                    totalDays++
                date = date.plusDays(1)
            }
            return totalDays
        }
    }
}

class DateTimeExt() {
    companion object {

        operator fun LocalDate.rangeTo(other: LocalDate): LocalDateRange {
            return LocalDateRange(this, other)
        }

        fun Date.toLocalDate(): LocalDate {
            Log.d("dateTo localDate",
                "$this ${LocalDate.of(this.year + 1900, this.month + 1, this.date)}")
            return LocalDate.of(this.year + 1900, this.month + 1, this.date)

        }

        fun Date.toLocalDateTime(): LocalDateTime {
            return LocalDateTime.of(this.year + 1900,
                this.month + 1,
                this.date,
                this.hours,
                this.minutes,
                this.seconds)
        }

        fun Date.toHumanReadTime(): String {
            return DateParser.simpleTimeParser.format(this) //
        }

        fun Date.toHumanDateAndTime(): String {
            return DateParser.simpleDateAndTimeParser.format(this) //
        }
    }
}

class LocalDateRange(override val start: LocalDate, override val endInclusive: LocalDate) :
    ClosedRange<LocalDate>, Iterable<LocalDate> {
    override fun iterator(): Iterator<LocalDate> {
        return DateIterator(start, endInclusive)
    }

}

class DateIterator(start: LocalDate, private val endInclusive: LocalDate) : Iterator<LocalDate> {

    private var current = start

    override fun hasNext(): Boolean {
        return current <= endInclusive
    }

    override fun next(): LocalDate {
        current = current.plusDays(1)
        return current
    }
}