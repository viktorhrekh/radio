package site.vie10.radio

import org.koin.core.component.KoinComponent
import site.vie10.radio.styles.Style

/**
 * @author vie10
 **/
interface Radio : KoinComponent {

    fun broadcast(style: Style, text: String): Boolean
}
