package site.vie10.radio.player

import org.bukkit.command.CommandSender
import org.bukkit.event.Listener

/**
 * @author vie10
 **/
class PlayerAdapter(
    private val commandSender: CommandSender
) : BasePlayer(commandSender.name, commandSender.name), Listener {

    override fun sendMessage(text: String) {
        commandSender.sendMessage(text)
    }

    override fun hasPermission(permission: String): Boolean = commandSender.hasPermission(permission)

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Player) return false
        if (other.id != id) return false
        return true
    }

    override fun hashCode(): Int = commandSender.hashCode()
}
