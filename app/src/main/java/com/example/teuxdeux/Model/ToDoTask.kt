package com.example.teuxdeux.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class ToDoTask(
    val id: Int = 0,
    val task: String,
    val type: String,
    val important: String = "no",
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val deadline: String? = null
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
