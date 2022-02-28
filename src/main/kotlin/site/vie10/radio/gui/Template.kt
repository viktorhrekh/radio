package site.vie10.radio.gui

/**
 * @author vie10
 **/
data class Template(
    val title: String,
    val size: Int,
    val items: LinkedHashMap<Set<Int>, Item>,
    val clickHandlers: Map<Set<Int>, List<ClickHandler>>
)
