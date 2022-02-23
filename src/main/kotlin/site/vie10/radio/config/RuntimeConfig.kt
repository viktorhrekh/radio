package site.vie10.radio.config

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberProperties

/**
 * @author vie10
 **/
@Suppress("UNCHECKED_CAST")
object RuntimeConfig {

    private val valuesMap: HashMap<String, Any?> = hashMapOf()
    private val KProperty1<*, *>.fullName
        get() = "${instanceParameter!!.type}.${name}"

    var <T> KProperty1<*, T>.runtimeVar: T
        get() {
            val value: T = valuesMap[fullName] as T
            if (value == null && !returnType.isMarkedNullable) {

                throw NullPointerException("$fullName is null while $returnType isn't nullable")
            }
            return value
        }
        set(value) {
            valuesMap[fullName] = value
        }

    @Synchronized
    fun KClass<*>.removeFromRuntime() {
        memberProperties.forEach { property ->
            val fullName = property.fullName
            valuesMap.remove(fullName)
        }
    }

    @Synchronized
    fun Any.uploadToRuntime() {
        this::class.memberProperties.forEach { property ->
            val fullName = property.fullName
            valuesMap[fullName] = property.call(this)
        }
    }

    @Synchronized
    fun clean() {
        valuesMap.clear()
    }
}
