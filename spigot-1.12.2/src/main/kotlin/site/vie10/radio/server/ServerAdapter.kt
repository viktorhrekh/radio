package site.vie10.radio.server

import org.bukkit.permissions.Permission
import org.koin.core.component.inject
import site.vie10.radio.commands.Command
import site.vie10.radio.commands.RadioCommandExecutor
import org.bukkit.Server as SpigotServer

/**
 * @author vie10
 **/
class ServerAdapter(
    private val server: SpigotServer
) : BaseServer() {

    private val commandExecutor: RadioCommandExecutor by inject()

    override fun registerCommand(command: Command) {
        val perm = Permission(command.permission)
        server.pluginManager.addPermission(perm)
        commandExecutor.registerCommand(command)
    }

    override fun unregisterCommand(command: Command) {
        server.pluginManager.removePermission(command.permission)
        commandExecutor.unregisterCommand(command)
    }

    override fun broadcast(input: String) {
        server.broadcastMessage(input)
    }
}
