package site.vie10.radio

import org.koin.core.component.inject
import site.vie10.radio.logging.Logger
import site.vie10.radio.styles.Style

/**
 * @author vie10
 **/
abstract class BaseRadio : Radio {

    private val log: Logger by inject()

    final override fun broadcast(style: Style, text: String): Boolean {
        runCatching {
            val styledInput = style.format(text)
            broadcastStyled(styledInput)
        }.onFailure {
            log.warn(it) { "Unexpected exception has occurred while broadcasting." }
            return false
        }

        return true
    }

    abstract fun broadcastStyled(input: List<String>)
}
