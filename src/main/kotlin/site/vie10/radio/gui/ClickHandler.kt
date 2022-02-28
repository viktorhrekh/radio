package site.vie10.radio.gui

import org.koin.core.component.KoinComponent
import site.vie10.radio.player.Player

/**
 * @author vie10
 **/
sealed interface ClickHandler : KoinComponent {

    fun handle(gui: GUI, whoClicked: Player, slotIndex: Int)
}
