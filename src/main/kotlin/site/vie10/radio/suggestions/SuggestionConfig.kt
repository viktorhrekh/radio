package site.vie10.radio.suggestions

import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigInfo.Companion.COMMON_GROUP
import kotlin.reflect.jvm.jvmName

/**
 * @author vie10
 **/
data class SuggestionConfig(
    val maxPerPlayer: Int,
    val dateTimeFormat: String
) {

    companion object {
        val Info = ConfigInfo(name = "suggestions", group = COMMON_GROUP, className = SuggestionConfig::class.jvmName)
    }
}
