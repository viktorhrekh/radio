package site.vie10.radio.gui

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.player.Player
import java.io.Closeable

/**
 * @author vie10
 **/
interface GUI : KoinComponent, Closeable {

    val title: String
    val size: Int

    fun showFor(player: Player)

    fun closeFor(player: Player)

    fun click(whoClicked: Player, slotIndex: Int)

    fun addClickHandler(slotIndex: Int, clickHandler: ClickHandler)

    @Suppress("unused")
    object Close : ClickHandler {
        override fun handle(gui: GUI, whoClicked: Player, slotIndex: Int) {
            gui.close()
        }
    }

    @Suppress("unused")
    data class Execute(
        val command: String
    ) : ClickHandler {

        override fun handle(gui: GUI, whoClicked: Player, slotIndex: Int) {
            if (gui !is ViewDataGUI) return
            val data = gui.dataAt(slotIndex) ?: return
            whoClicked.performCommand(data.applyPlaceholders(command))
        }
    }

    @Suppress("unused")
    data class OpenGUI(
        val gui: String
    ) : ClickHandler {
        private val guiFactory: GUIFactory by inject()

        override fun handle(gui: GUI, whoClicked: Player, slotIndex: Int) {
            if (gui !is ViewDataGUI) return
            val template = GUIConfig::templates.runtimeVar.getOrElse(this.gui) { return }
            val data = gui.dataAt(slotIndex) ?: return
            guiFactory.create(template, data).apply {
                render()
                showFor(whoClicked)
            }
        }
    }
}
