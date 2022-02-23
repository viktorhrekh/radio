package site.vie10.radio.styles

import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigInfo.Companion.COMMON_GROUP
import kotlin.reflect.jvm.jvmName

/**
 * @author vie10
 **/
data class StyleConfig(
    val styles: Map<String, ConfigurableStyle>
) {

    companion object {
        val Info = ConfigInfo(name = "styles", group = COMMON_GROUP, className = StyleConfig::class.jvmName)
    }
}
