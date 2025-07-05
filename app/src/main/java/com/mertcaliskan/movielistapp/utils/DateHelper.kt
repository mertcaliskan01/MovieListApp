package com.mertcaliskan.movielistapp.utils

import java.text.SimpleDateFormat
import java.util.Date

class DateHelper {
    companion object {
        fun getYearFromDate(dateString: String): Int {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = dateFormat.parse(dateString)
            return date.year + 1900
        }

        fun formatToDDMMYYYY(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd.MM.yyyy")

            val date: Date = inputFormat.parse(dateString)
            return outputFormat.format(date)
        }
    }
}