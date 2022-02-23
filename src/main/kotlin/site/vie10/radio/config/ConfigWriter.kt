package site.vie10.radio.config

import org.koin.core.component.KoinComponent

/**
 * @author vie10
 **/
interface ConfigWriter : KoinComponent {

    fun write(configInfo: ConfigInfo, byteArray: ByteArray)

    fun writeDefaultIfNotExists(configInfo: ConfigInfo)

    fun writeDefault(configInfo: ConfigInfo)
}
