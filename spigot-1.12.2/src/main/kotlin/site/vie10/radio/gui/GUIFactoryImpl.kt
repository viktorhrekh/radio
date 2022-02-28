package site.vie10.radio.gui

import org.bukkit.plugin.Plugin
import org.koin.core.component.inject

/**
 * @author vie10
 **/
class GUIFactoryImpl : GUIFactory {

    private val plugin: Plugin by inject()

    override fun create(template: Template, viewData: ViewData): SingleViewDataGUI {
        val instance = SingleViewDataGUIAdapter(template, viewData)
        plugin.apply { server.pluginManager.registerEvents(instance, this) }
        return instance
    }

    override fun create(template: Template, viewData: List<ViewData>): ViewDataListGUI {
        val instance = ViewDataListGUIAdapter(template, viewData)
        plugin.apply { server.pluginManager.registerEvents(instance, this) }
        return instance
    }
}
