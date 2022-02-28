package site.vie10.radio.gui

import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigInfo.Companion.COMMON_GROUP
import kotlin.reflect.jvm.jvmName

/**
 * @author vie10
 **/
data class GUIConfig(
    val templates: Map<String, Template>
) {

    companion object {
        val Info = ConfigInfo(name = "gui", group = COMMON_GROUP, className = GUIConfig::class.jvmName)
    }
}
