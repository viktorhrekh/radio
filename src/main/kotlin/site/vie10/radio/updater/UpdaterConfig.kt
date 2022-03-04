package site.vie10.radio.updater

import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigInfo.Companion.ADVANCE_GROUP
import kotlin.reflect.jvm.jvmName

/**
 * @author vie10
 **/
data class UpdaterConfig(
    val enabled: Boolean,
    val delay: Long,
    val download: Boolean,
) {

    companion object {
        val Info = ConfigInfo("updater", group = ADVANCE_GROUP, className = UpdaterConfig::class.jvmName)
    }
}
