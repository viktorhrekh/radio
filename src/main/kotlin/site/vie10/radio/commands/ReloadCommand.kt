package site.vie10.radio.commands

import kotlinx.coroutines.launch
import org.koin.core.component.inject
import site.vie10.radio.RadioPlugin
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.utils.commandMessages
import site.vie10.radio.utils.commonMessages
import kotlin.system.measureTimeMillis

/**
 * @author vie10
 **/
class ReloadCommand : BaseCommand(
    CommandConfig::reload.runtimeVar,
    { commandMessages.reloadHelp }
) {

    private val radioPlugin: RadioPlugin by inject()

    override fun onExecute(player: Player, args: List<String>) {
        radioPlugin.scope.launch {
            val measuredMillis = measureTimeMillis { radioPlugin.reloadConfig() }
            player.applyPlaceholdersAndSendMessage(commonMessages.reloaded, "millis" to measuredMillis)
        }
    }
}
