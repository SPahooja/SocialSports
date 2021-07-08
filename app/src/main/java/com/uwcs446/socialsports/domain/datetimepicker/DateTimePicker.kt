package com.uwcs446.socialsports.domain.datetimepicker

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class DateTimePicker {
    fun getDatePicker(): MaterialDatePicker<Long> {
        val constraintBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintBuilder)
            .build()
    }

    fun getTimePicker(): MaterialTimePicker {
        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Select Time")
            .build()
    }
}
