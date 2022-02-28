package site.vie10.radio.commands

import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigInfo.Companion.ADVANCE_GROUP
import kotlin.reflect.jvm.jvmName

/**
 * @author vie10
 **/
data class CommandConfig(
    val broadcast: CommandInfoConfig,
    val suggest: CommandInfoConfig,
    val styles: CommandInfoConfig,
    val suggestions: CommandInfoConfig,
    val approve: CommandInfoConfig,
    val decline: CommandInfoConfig,
    val reload: CommandInfoConfig,
    val gui: CommandInfoConfig
) {

    data class CommandInfoConfig(
        val name: String,
        val permission: String
    )

    companion object {
        val Info = ConfigInfo(name = "commands", group = ADVANCE_GROUP, className = CommandConfig::class.jvmName)
    }
}
