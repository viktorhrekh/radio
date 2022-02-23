package site.vie10.radio.config

import org.koin.core.component.KoinComponent

/**
 * @author vie10
 **/
interface ConfigLoader : KoinComponent {

    fun load(configInfo: ConfigInfo): Result<Any>
}
