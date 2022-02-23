package site.vie10.radio.commands

import org.koin.core.component.inject
import site.vie10.radio.Radio
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.styles.StyleConfig
import site.vie10.radio.utils.commandMessages
import site.vie10.radio.utils.commonMessages

/**
 * @author vie10
 **/
class BroadcastCommand : BaseCommand(
    CommandConfig::broadcast.runtimeVar,
    { commandMessages.broadcastHelp },
    { true },
    { true }
) {

    private val radio: Radio by inject()

    override fun onExecute(player: Player, args: List<String>) {
        val styleName = args[0]
        val style = StyleConfig::styles.runtimeVar.getOrElse(styleName) {
            player.applyPlaceholdersAndSendMessage(commonMessages.styleNotExists, "style" to styleName)
            return
        }
        if (!player.hasPermission(style.permission)) {
            player.sendHaveNotEnoughPermissionsMessage()
            return
        }
        val input = args.subList(1, args.size).joinToString(" ")
        radio.broadcast(style, input)
    }

    override fun onTabComplete(player: Player, args: List<String>): List<String> {
        if (args.size == 1) {
            return StyleConfig::styles.runtimeVar.map { it.key }
        }
        return emptyList()
    }
}
