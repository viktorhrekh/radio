package site.vie10.radio.config

import kotlin.reflect.KClass

/**
 * @author vie10
 **/
@Suppress("MemberVisibilityCanBePrivate")
data class ConfigInfo(
    val name: String,
    val className: String,
    val group: String = NO_GROUP,
    val extension: String = DEFAULT_EXTENSION,
) {

    val clazz: KClass<out Any> by lazy { Class.forName(className).kotlin }
    val pathWithExtension by lazy { "$path.$extension" }
    val path: String by lazy { if (group.isEmpty()) name else "$group/$name" }

    companion object {
        const val NO_GROUP = ""
        const val COMMON_GROUP = "common"
        const val ADVANCE_GROUP = "advance"
        const val DEFAULT_EXTENSION = "yml"
    }
}
