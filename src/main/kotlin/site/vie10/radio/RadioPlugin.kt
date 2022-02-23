package site.vie10.radio

import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

/**
 * @author vie10
 **/
interface RadioPlugin : KoinComponent {

    val scope: CoroutineScope

    suspend fun start()

    suspend fun stop()

    suspend fun reloadConfig()

    companion object {
        const val COROUTINE_SCOPE_NAME = "vie10-radio"
    }
}
