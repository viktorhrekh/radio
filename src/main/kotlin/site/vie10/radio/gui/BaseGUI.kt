package site.vie10.radio.gui

import org.koin.core.component.inject
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.logging.Logger
import site.vie10.radio.messages.MessageConfig
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.utils.newConcurrentHashSet
import java.util.concurrent.ConcurrentHashMap

/**
 * @author vie10
 **/
abstract class BaseGUI : GUI {

    protected val viewers: MutableSet<Player> = newConcurrentHashSet()
    private val clickHandlers: MutableMap<Int, MutableSet<ClickHandler>> = ConcurrentHashMap()
    private val log: Logger by inject()

    final override fun showFor(player: Player) {
        viewers.add(player)
        runCatching {
            onShowFor(player)
        }.onFailure {
            if (it is ImpossibleOpenGUIException) {
                player.applyPlaceholdersAndSendMessage(MessageConfig::common.runtimeVar.impossibleOpenGUI)
            } else {
                log.warn(it) { "Showing $this for $player failed by unexpected exception." }
            }
        }
    }

    abstract fun onShowFor(player: Player)

    final override fun closeFor(player: Player) {
        viewers.remove(player)
        runCatching {
            onCloseFor(player)
        }.onFailure {
            log.warn(it) { "Closing $this for $player failed by unexpected exception." }
        }
    }

    abstract fun onCloseFor(player: Player)

    final override fun close() {
        viewers.parallelStream().forEach {
            closeFor(it)
        }
        onClose()
    }

    abstract fun onClose()

    final override fun click(whoClicked: Player, slotIndex: Int) {
        clickHandlers.getOrElse(slotIndex) { return }.forEach {
            it.handle(this, whoClicked, slotIndex)
        }
    }

    final override fun addClickHandler(slotIndex: Int, clickHandler: ClickHandler) {
        clickHandlers.computeIfAbsent(slotIndex) { linkedSetOf() }.add(clickHandler)
    }
}
