package site.vie10.radio.utils

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author vie10
 **/

fun <T> newConcurrentHashSet(): MutableSet<T> = Collections.newSetFromMap(ConcurrentHashMap())
