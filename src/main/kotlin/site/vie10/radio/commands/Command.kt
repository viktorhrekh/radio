package site.vie10.radio.commands

import org.koin.core.component.KoinComponent
import site.vie10.radio.player.Player

/**
 * @author vie10
 **/
interface Command : KoinComponent {

    val name: String
    val permission: String

    fun execute(player: Player, args: List<String>)

    fun tabComplete(player: Player, args: List<String>): List<String>
}
