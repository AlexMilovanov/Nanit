package com.nanit.happybirthday.model

import androidx.room.Entity
import java.util.*

@Entity(primaryKeys = ["name", "birthday"])
data class Baby(val name: String, val birthday: Date, val photoPath: String?)