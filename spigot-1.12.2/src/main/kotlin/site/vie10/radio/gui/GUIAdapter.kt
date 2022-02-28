package site.vie10.radio.gui

import org.bukkit.Bukkit
import org.bukkit.DyeColor
import org.bukkit.Material
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

    private fun Item.asBukkit(): Result<ItemStack> = runCatching {
        val material = Material.valueOf(id.uppercase())
        ItemStack(material, count).apply {
            val newMeta = itemMeta
            newMeta.displayName = name
            newMeta.lore = lore
            itemMeta = newMeta
            runCatching { DyeColor.valueOf(color.uppercase()) }.onSuccess {
                durability = it.ordinal.toShort()
            }
        }
    }

    private fun Item.asBukkitWithViewData(viewData: ViewData): Result<ItemStack> = runCatching {
        val material = Material.valueOf(id.uppercase())
        ItemStack(material, count).apply {
            val newMeta = itemMeta
            newMeta.displayName = viewData.applyPlaceholders(name)
            newMeta.lore = viewData.applyPlaceholders(lore)
            itemMeta = newMeta
            runCatching { DyeColor.valueOf(color.uppercase()) }.onSuccess {
                durability = it.ordinal.toShort()
            }
        }
    }
}
