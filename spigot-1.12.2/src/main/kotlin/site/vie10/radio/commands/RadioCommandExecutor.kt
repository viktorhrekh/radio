package site.vie10.radio.commands

import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.messages.MessageConfig
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.player.PlayerAdapter
import org.bukkit.command.Command as SpigotCommand

/**
 * @author vie10
 **/
class RadioCommandExecutor :
    CommandExecutor,
    TabCompleter {

    private val commands: MutableMap<String, Command> = hashMapOf()

    override fun onTabComplete(
        commandSender: CommandSender,
        spigotCommand: SpigotCommand,
        alias: String?,
        args: Array<out String>
    ): MutableList<String> {
        val player = PlayerAdapter(commandSender)
        if (args.isEmpty()) {
            return commandsTabComplete(null)
        }
        val commandName = args[0].lowercase()
        val command = commands.getOrElse(commandName) {
            return commandsTabComplete(commandName)
        }
        val argsWithoutCommandName: List<String> = if (args.size == 1) {
            emptyList()
        } else args.copyOfRange(1, args.size).toList()
        return command.tabComplete(player, argsWithoutCommandName)
            .mapByStartsWithIfNotNull(argsWithoutCommandName.lastOrNull()).toMutableList()
    }

    private fun commandsTabComplete(phrase: String?): MutableList<String> = commands.map { it.key }
        .mapByStartsWithIfNotNull(phrase).toMutableList()

    private fun List<String>.mapByStartsWithIfNotNull(phrase: String?): List<String> {
        return if (phrase != null) {
            filter { it.startsWith(phrase, ignoreCase = true) }
        } else this
    }

    override fun onCommand(
        commandSender: CommandSender,
        spigotCommand: SpigotCommand,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = PlayerAdapter(commandSender)
        if (args.isEmpty()) {
            player.sendHelp()
            return true
        }
        val commandName = args[0].lowercase()
        val command = commands.getOrElse(commandName) {
            player.sendHelp()
            return true
        }
        val argsWithoutCommandName: List<String> = if (args.size == 1) {
            emptyList()
        } else args.copyOfRange(1, args.size).toList()
        command.execute(player, argsWithoutCommandName.toList())
        return true
    }

    private fun Player.sendHelp() {
        applyPlaceholdersAndSendMessage(MessageConfig::commands.runtimeVar.help)
    }

    @Synchronized
    fun registerCommand(command: Command) {
        commands[command.name.lowercase()] = command
    }

    @Synchronized
    fun unregisterCommand(command: Command) {
        commands.remove(command.name.lowercase())
    }
}
