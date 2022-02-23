package site.vie10.radio.utils

import java.time.Instant
import java.util.*

/**
 * @author vie10
 **/

val nowUnixTimestamp: Long
    get() = System.currentTimeMillis() / 1000

val Long.asDate: Date
    get() = Date.from(Instant.ofEpochSecond(this))
