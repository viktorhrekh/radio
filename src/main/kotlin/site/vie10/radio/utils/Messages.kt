package site.vie10.radio.utils

import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.messages.MessageConfig

/**
 * @author vie10
 **/

val commonMessages: MessageConfig.CommonMessages
    get() = MessageConfig::common.runtimeVar

val commandMessages: MessageConfig.CommandsMessages
    get() = MessageConfig::commands.runtimeVar
