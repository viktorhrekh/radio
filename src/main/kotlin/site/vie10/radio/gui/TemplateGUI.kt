package site.vie10.radio.gui

/**
 * @author vie10
 **/
abstract class TemplateGUI(
    protected val template: Template
) : BaseGUI() {

    final override val title: String = template.title
    final override val size: Int = template.size

    fun renderTemplate() {
        template.run {
            items.forEach { (slotIndexes, item) ->
                slotIndexes.forEach { bindItem(it, item) }
            }
            clickHandlers.forEach { (slotIndexes, clickHandlers) ->
                slotIndexes.forEach {
                    clickHandlers.forEach { clickHandler ->
                        addClickHandler(it, clickHandler)
                    }
                }
            }
        }
    }

    protected abstract fun bindItem(slotIndex: Int, item: Item)
}
