package site.vie10.radio.gui

/**
 * @author vie10
 **/
data class Item(
    val id: String,
    val count: Int = 1,
    val name: String = "",
    val lore: List<String> = emptyList(),
    val color: String = "",
    val data: Boolean = false
)
