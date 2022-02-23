package site.vie10.radio.messages

import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigInfo.Companion.COMMON_GROUP
import kotlin.reflect.jvm.jvmName

/**
 * @author vie10
 **/
data class MessageConfig(
    val commands: CommandsMessages,
    val common: CommonMessages
) {

    data class CommonMessages(
        val unableOpenGUI: List<String>,
        val approved: List<String>,
        val declined: List<String>,
        val suggestionNotFound: List<String>,
        val noSuggestions: List<String>,
        val unexpectedException: List<String>,
        val suggestionSent: List<String>,
        val broadcastSent: List<String>,
        val styleNotExists: List<String>,
        val youDoNotHavePerm: List<String>,
        val youHaveReachedMaxSuggestions: List<String>,
        val suggestion: List<String>,
        val reloaded: List<String>
    )

    data class CommandsMessages(
        val help: List<String>,
        val suggestHelp: List<String>,
        val broadcastHelp: List<String>,
        val stylesHelp: List<String>,
        val suggestionsHelp: List<String>,
        val approveHelp: List<String>,
        val declineHelp: List<String>,
        val reloadHelp: List<String>
    )

    companion object {
        val Info = ConfigInfo(name = "messages", group = COMMON_GROUP, className = MessageConfig::class.jvmName)
    }
}
