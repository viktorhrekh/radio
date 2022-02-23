package site.vie10.radio.commands

import org.koin.core.component.inject
import site.vie10.radio.logging.Logger
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.utils.commonMessages

/**
 * @author vie10
 **/
abstract class BaseCommand(
    commandConfig: CommandConfig.CommandInfoConfig,
    private val help: () -> List<String>,
    private vararg val argValidators: (String) -> Boolean
) : Command {

    private val log: Logger by inject()
    final override val name: String = commandConfig.name
    final override val permission: String = commandConfig.permission
    private val minArgsCount: Int = argValidators.size

    private fun verifyArgument(index: Int, arg: String): Boolean = argValidators[index](arg)

    final override fun tabComplete(player: Player, args: List<String>): List<String> {
        return runCatching {
            onTabComplete(player, args)
        }.getOrElse {
            log.warn(it) { "Unexpected exception has occurred while tab completing $this" }
            emptyList()
        }
    }

    protected open fun onTabComplete(player: Player, args: List<String>): List<String> = emptyList()

    final override fun execute(player: Player, args: List<String>) {
        if (!player.hasPermission(permission)) {
            player.sendHaveNotEnoughPermissionsMessage()
            return
        }
        if (args.size < minArgsCount) {
            player.sendHelp()
            return
        }
        argValidators.forEachIndexed { index, _ ->
            val arg = args[index]
            if (!verifyArgument(index, arg)) {
                player.sendHelp()
                return
            }
        }
        runCatching { onExecute(player, args) }.onFailure {
            log.warn(it) { "Unexpected exception has occurred while executing $this" }
            player.sendUnexpectedExceptionMessage()
        }
    }

    protected fun Player.sendHaveNotEnoughPermissionsMessage() {
        applyPlaceholdersAndSendMessage(commonMessages.youDoNotHavePerm)
    }

    private fun Player.sendUnexpectedExceptionMessage() {
        applyPlaceholdersAndSendMessage(commonMessages.unexpectedException)
    }

    private fun Player.sendHelp() {
        applyPlaceholdersAndSendMessage(help())
    }

    protected abstract fun onExecute(player: Player, args: List<String>)
}
