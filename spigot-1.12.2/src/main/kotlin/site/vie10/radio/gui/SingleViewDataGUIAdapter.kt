package site.vie10.radio.gui

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import org.koin.core.component.inject
import site.vie10.radio.player.Player
import site.vie10.radio.player.PlayerAdapter

/**
 * @author vie10
 **/
class SingleViewDataGUIAdapter(
    template: Template,
    viewData: ViewData
) : SingleViewDataGUI(template, viewData), Listener {

    private val guiAdapter: GUIAdapter = GUIAdapter(title, size)
    private val plugin: Plugin by inject()
    private val inventory: Inventory by lazy { Bukkit.createInventory(null, size, title) }

    override fun renderItem(slotIndexes: Set<Int>, item: Item, viewData: ViewData?) {
        guiAdapter.renderItem(slotIndexes, item, viewData)
    }

    override fun bindItem(slotIndex: Int, item: Item) {
        guiAdapter.bindItem(slotIndex, item)
    }

    override fun onShowFor(player: Player) {
        guiAdapter.onShowFor(player)
    }

    override fun onCloseFor(player: Player) {
        guiAdapter.onCloseFor(player)
    }

    @EventHandler
    fun onClickEvent(event: InventoryClickEvent) {
        if (event.inventory != inventory) return
        event.isCancelled = true
        val playerAdapter = PlayerAdapter(event.whoClicked)
        click(playerAdapter, event.slot)
    }

    @EventHandler
    fun onQuitEvent(event: PlayerQuitEvent) {
        val playerAdapter = PlayerAdapter(event.player)
        if (!viewers.contains(playerAdapter)) return
        closeFor(playerAdapter)
    }

    @EventHandler
    fun onCloseEvent(event: InventoryCloseEvent) {
        val playerAdapter = PlayerAdapter(event.player)
        if (!viewers.contains(playerAdapter)) return
        closeFor(playerAdapter)
    }

    @EventHandler
    fun onMoveEvent(event: InventoryDragEvent) {
        if (event.inventory != inventory) return
        event.isCancelled = true
    }

    @EventHandler
    fun onDisableEvent(event: PluginDisableEvent) {
        if (event.plugin != plugin) return
        close()
    }
}
