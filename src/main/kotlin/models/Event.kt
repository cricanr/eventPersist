package models

import com.google.gson.Gson

data class Event(val timestamp: Long, val userId: Int, val event: String)

private val gson = Gson()

fun Event.toJson(): String = gson.toJson(this)

fun EventPBOuterClass.EventsPB.EventPB.toEntity(): Event =
    Event(
        timestamp = timestamp,
        userId = userId,
        event = event
    )