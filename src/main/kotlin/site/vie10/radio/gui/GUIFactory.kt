package site.vie10.radio.gui

import org.koin.core.component.KoinComponent

/**
 * @author vie10
 **/
interface GUIFactory : KoinComponent {

    fun create(template: Template, viewData: ViewData): SingleViewDataGUI

    fun create(template: Template, viewData: List<ViewData>): ViewDataListGUI
}
