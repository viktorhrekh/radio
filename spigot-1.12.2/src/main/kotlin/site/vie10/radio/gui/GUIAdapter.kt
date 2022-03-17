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
        }.onFailure {
            log.warn(it) { "$item has invalid configuration" }
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

    private fun Item.asBukkit(): Result<ItemStack> = newItemStack(id, count, name, lore, color)

    private fun Item.asBukkitWithViewData(viewData: ViewData): Result<ItemStack> =
        newItemStack(id, count, viewData.applyPlaceholders(name), viewData.applyPlaceholders(lore), color)

    private fun newItemStack(
        id: String,
        count: Int,
        name: String,
        lore: List<String>,
        color: String,
    ): Result<ItemStack> = runCatching {
        val material = Material.valueOf(id.uppercase())
        ItemStack(material, count).apply {
            lore(lore)
            displayName(name)
            color(color)
        }
    }

    private fun ItemStack.color(newColor: String) {
        if (newColor.isEmpty()) return
        durability = DyeColor.valueOf(newColor.uppercase()).ordinal.toShort()
    }

    private fun ItemStack.displayName(newName: String) {
        itemMeta = itemMeta?.apply {
            @Suppress("UsePropertyAccessSyntax")
            setDisplayName(newName) // Use property access for support 1.14+.
        }
    }

    private fun ItemStack.lore(newLore: List<String>) {
        itemMeta = itemMeta?.apply {
            lore = newLore
        }
    }
}
