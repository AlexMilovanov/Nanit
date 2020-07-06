package com.nanit.happybirthday.util

import org.joda.time.*

private const val YEAR_IN_MONTHS = 12
private const val YEAR_IN_WEEKS = 52
private const val MONTH_IN_WEEKS = 4

// Helper method returning the triple of values - years, months and weeks
fun calculateAgeYearMonths(birthdayMillis: Long): Triple<Int, Int, Int> {
    val today = LocalDate.now()
    val birthday = LocalDate(Instant.ofEpochMilli(birthdayMillis))

    val years = Years.yearsBetween(birthday, today).years
    val months = Months.monthsBetween(birthday, today).months - years * YEAR_IN_MONTHS
    val weeks = Weeks.weeksBetween(birthday, today).weeks - years * YEAR_IN_WEEKS - months * MONTH_IN_WEEKS

    return Triple(years, months, weeks)
}