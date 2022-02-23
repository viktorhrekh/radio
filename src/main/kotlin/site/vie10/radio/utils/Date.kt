package site.vie10.radio.utils

import java.time.Instant
import java.util.*

val currentDate: Date
    get() = Date.from(Instant.now())
