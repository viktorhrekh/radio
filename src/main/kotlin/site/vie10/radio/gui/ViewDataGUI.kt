package site.vie10.radio.gui

/**
 * @author vie10
 **/
abstract class ViewDataGUI(template: Template) : TemplateGUI(template) {

    protected val dataItems: Map<Set<Int>, Item>
        get() = template.items.filter { it.value.data }

    fun render() {
        renderTemplate()
        renderDataItems()
    }

    abstract fun renderDataItems()

    abstract fun dataAt(slotIndex: Int): ViewData?

    protected abstract fun renderItem(slotIndexes: Set<Int>, item: Item, viewData: ViewData?)
}
