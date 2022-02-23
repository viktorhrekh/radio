package site.vie10.radio.commands

import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.player.Player
import site.vie10.radio.styles.StyleConfig
import site.vie10.radio.utils.commandMessages

/**
 * @author vie10
 **/
class StylesCommand : BaseCommand(
    CommandConfig::styles.runtimeVar,
    { commandMessages.stylesHelp }
) {

    override fun onExecute(player: Player, args: List<String>) {
        val messageBuilder = StringBuilder()
        StyleConfig::styles.runtimeVar.forEach { (name, style) ->
            messageBuilder.append(name).append(":").appendLine()
            style.format("example").forEach { messageBuilder.append(it).appendLine() }
        }
        player.sendMessage(messageBuilder.toString())
    }
}
