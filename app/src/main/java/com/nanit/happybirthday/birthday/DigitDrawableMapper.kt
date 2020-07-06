package com.nanit.happybirthday.birthday

import com.nanit.happybirthday.R
import java.lang.IllegalArgumentException

private val map by lazy {
    HashMap<Int, Int> ().apply {
        put(0, R.drawable.img_number_zero)
        put(1, R.drawable.img_number_one)
        put(2, R.drawable.img_number_two)
        put(3, R.drawable.img_number_three)
        put(4, R.drawable.img_number_four)
        put(5, R.drawable.img_number_five)
        put(6, R.drawable.img_number_six)
        put(7, R.drawable.img_number_seven)
        put(8, R.drawable.img_number_eight)
        put(9, R.drawable.img_number_nine)
    }
}

fun getDigitDrawableResId(digit: Int): Int {
    if (digit !in 0..9) {
        throw IllegalArgumentException("Invalid Input $digit")
    }
    return map[digit]!!
}

fun getOneAndHalfDrawableResId(): Int = R.drawable.img_number_one_and_half


