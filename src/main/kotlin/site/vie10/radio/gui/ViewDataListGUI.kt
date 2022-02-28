package site.vie10.radio.gui

import site.vie10.radio.player.Player
import kotlin.math.ceil
import kotlin.math.max

/**
 * @author vie10
 **/
abstract class ViewDataListGUI(
    template: Template,
    private val viewData: List<ViewData>
) : ViewDataGUI(template) {

    private var page: Int = 1
    private val itemsPerPage: Int
        get() = template.items.values.count { it.data }
    private val maxPage: Int
        get() = if (itemsPerPage == 0) 0 else max(ceil(viewData.size.toFloat() / itemsPerPage).toInt(), 1)
    private val pageStartsFromIndex: Int
        get() = page * itemsPerPage - itemsPerPage

    @Synchronized
    override fun renderDataItems() {
        val maxPage = maxPage
        if (maxPage < page) {
            renderPage(maxPage)
        } else {
            renderPage()
        }
    }

    fun nextPage() {
        renderPage(page + 1)
    }

    fun prevPage() {
        renderPage(page - 1)
    }

    @Synchronized
    private fun renderPage(page: Int) {
        if (page < 1) return
        if (page > maxPage) return
        this.page = page

        renderPage()
    }

    private fun renderPage() {
        val pageStartsFromIndex: Int = pageStartsFromIndex
        dataItems.toList().forEachIndexed { index, item ->
            val viewData = viewData.getOrNull(pageStartsFromIndex + index)
            renderItem(item.first, item.second, viewData)
        }
    }

    @Synchronized
    override fun dataAt(slotIndex: Int): ViewData? {
        for (entry in template.items) {
            val dataItems = dataItems.toList()
            val dataItemPosition = dataItems.indexOfFirst { it.first.contains(slotIndex) }
            if (entry.key.contains(slotIndex) && entry.value.data) return viewData.getOrNull(pageStartsFromIndex + dataItemPosition)
        }
        return null
    }

    @Suppress("unused")
    object PreviousPage : ClickHandler {
        override fun handle(gui: GUI, whoClicked: Player, slotIndex: Int) {
            if (gui is ViewDataListGUI) gui.prevPage()
        }
    }

    @Suppress("unused")
    object NextPage : ClickHandler {
        override fun handle(gui: GUI, whoClicked: Player, slotIndex: Int) {
            if (gui is ViewDataListGUI) gui.nextPage()
        }
    }
}
