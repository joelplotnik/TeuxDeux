package com.example.teuxdeux.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class ToDoTask(
    val id: Int? = 0,
    val user: String? = null,
    val task: String? = null,
    val type: String? = null,
    val important: Boolean? = null,
    val completed: Boolean? = null,
    val created: Long = System.currentTimeMillis(),
    val deadline: String? = null
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
