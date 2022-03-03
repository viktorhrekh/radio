package site.vie10.radio.gui

import org.bukkit.Bukkit
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import site.vie10.radio.logging.Logger
import site.vie10.radio.player.Player
import site.vie10.radio.player.PlayerAdapter

/**
 * @author vie10
 **/
class GUIAdapter(title: String, size: Int) : KoinComponent {

    private val log: Logger by inject()
    private val inventory: Inventory by lazy { Bukkit.createInventory(null, size, title) }

    fun isEventRelatedTo(event: InventoryEvent): Boolean = event.inventory == inventory

    fun isEventRelatedTo(event: InventoryMoveItemEvent): Boolean =
        event.source == inventory || event.destination == inventory

    fun renderItem(slotIndexes: Set<Int>, item: Item, viewData: ViewData?) {
        if (viewData == null) {
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

    fun bindItem(slotIndex: Int, item: Item) {
        if (item.data) return
        item.asBukkit().onSuccess {
            inventory.setItem(slotIndex, it)
        }.onFailure {
            log.warn(it) { "$item has invalid configuration" }
        }
    }

    fun onShowFor(player: Player) {
        if (player !is PlayerAdapter) throw IllegalStateException()
        val spigotPlayer = player.spigotPlayer.getOrElse { throw ImpossibleOpenGUIException() }
        spigotPlayer.openInventory(inventory)
    }

    fun onCloseFor(player: Player) {
        if (player !is PlayerAdapter) throw IllegalStateException()
        val spigotPlayer = player.spigotPlayer.getOrThrow()
        if (spigotPlayer.openInventory.topInventory == inventory) spigotPlayer.closeInventory()
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun Item.asBukkit(): Result<ItemStack> = runCatching {
        val material = Material.valueOf(id.uppercase())
        ItemStack(material, count).apply {
            val newMeta = itemMeta
            newMeta?.setDisplayName(name)
            newMeta?.lore = lore
            itemMeta = newMeta
            runCatching {
                val dyeColor = DyeColor.valueOf(color.uppercase())
                durability = dyeColor.ordinal.toShort()
            }
        }
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun Item.asBukkitWithViewData(viewData: ViewData): Result<ItemStack> = runCatching {
        val material = Material.valueOf(id.uppercase())
        ItemStack(material, count).apply {
            val newMeta = itemMeta
            newMeta?.setDisplayName(viewData.applyPlaceholders(name))
            newMeta?.lore = viewData.applyPlaceholders(lore)
            itemMeta = newMeta
            runCatching {
                val dyeColor = DyeColor.valueOf(color.uppercase())
                durability = dyeColor.ordinal.toShort()
            }
        }
    }
}
