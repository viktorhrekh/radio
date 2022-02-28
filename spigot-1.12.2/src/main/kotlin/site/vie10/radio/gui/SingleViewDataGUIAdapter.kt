package site.vie10.radio.gui

import org.bukkit.Bukkit
import org.bukkit.DyeColor
import org.bukkit.Material
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
import org.bukkit.inventory.ItemStack as BukkitItem

/**
 * @author vie10
 **/
class SingleViewDataGUIAdapter(
    template: Template,
    viewData: ViewData
) : SingleViewDataGUI(template, viewData), Listener {

    private val plugin: Plugin by inject()
    private val inventory: Inventory by lazy { Bukkit.createInventory(null, size, title) }

    override fun renderItem(slotIndexes: Set<Int>, item: Item, viewData: ViewData?) {
        if (viewData == null)  {
            slotIndexes.forEach {
                inventory.setItem(it, null)
            }
            return
        }
        item.asBukkitWithViewData(viewData).onSuccess {
            slotIndexes.forEach { slotIndex ->
                inventory.setItem(slotIndex, it)
            }
        }
    }

    override fun bindItem(slotIndex: Int, item: Item) {
        if (item.data) return
        item.asBukkit().onSuccess {
            inventory.setItem(slotIndex, it)
        }.onFailure {
            log.warn(it) { "$item has invalid configuration" }
        }
    }

    override fun onShowFor(player: Player) {
        if (player !is PlayerAdapter) throw IllegalStateException()
        val spigotPlayer = player.spigotPlayer.getOrElse { throw ImpossibleOpenGUIException() }
        spigotPlayer.openInventory(inventory)
    }

    override fun onCloseFor(player: Player) {
        if (player !is PlayerAdapter) throw IllegalStateException()
        val spigotPlayer = player.spigotPlayer.getOrThrow()
        if (spigotPlayer.openInventory.topInventory == inventory) spigotPlayer.closeInventory()
    }

    private fun Item.asBukkit(): Result<BukkitItem> = runCatching {
        val material = Material.valueOf(id.uppercase())
        BukkitItem(material, count).apply {
            val newMeta = itemMeta
            newMeta.displayName = name
            newMeta.lore = lore
            itemMeta = newMeta
            runCatching { DyeColor.valueOf(color.uppercase()) }.onSuccess {
                durability = it.ordinal.toShort()
            }
        }
    }

    private fun Item.asBukkitWithViewData(viewData: ViewData): Result<BukkitItem> = runCatching {
        val material = Material.valueOf(id.uppercase())
        BukkitItem(material, count).apply {
            val newMeta = itemMeta
            newMeta.displayName = viewData.applyPlaceholders(name)
            newMeta.lore = viewData.applyPlaceholders(lore)
            itemMeta = newMeta
            runCatching { DyeColor.valueOf(color.uppercase()) }.onSuccess {
                durability = it.ordinal.toShort()
            }
        }
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
        if (!viewers.contains(playerAdapter) || event.inventory != inventory) return
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
