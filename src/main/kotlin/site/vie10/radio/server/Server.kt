package site.vie10.radio.server

import org.koin.core.component.KoinComponent
import site.vie10.radio.commands.Command

/**
 * @author vie10
 **/
interface Server : KoinComponent {

    fun registerCommand(command: Command)

    fun unregisterCommand(command: Command)

    fun broadcast(input: List<String>)

    fun broadcast(input: String)
}
