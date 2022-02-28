package site.vie10.radio.gui

/**
 * @author vie10
 **/
abstract class SingleViewDataGUI(
    template: Template,
    private val viewData: ViewData
) : ViewDataGUI(template) {

    @Synchronized
    override fun renderDataItems() {
        dataItems.forEach { (slots, item) -> renderItem(slots, item, viewData) }
    }

    @Synchronized
    override fun dataAt(slotIndex: Int): ViewData = viewData
}
