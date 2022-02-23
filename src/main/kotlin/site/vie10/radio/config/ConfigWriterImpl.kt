package site.vie10.radio.config

import org.koin.core.component.inject
import site.vie10.radio.logging.Logger

class ConfigWriterImpl(
    private val userConfigFileProvider: ConfigFileProvider,
    private val defaultConfigFileProvider: ConfigFileProvider
) : ConfigWriter {

    private val log: Logger by inject()

    override fun write(configInfo: ConfigInfo, byteArray: ByteArray) {
        userConfigFileProvider.provide(configInfo)
            .onSuccess { it.write(byteArray) }
            .onFailure { log.warn(it) }
    }

    override fun writeDefaultIfNotExists(configInfo: ConfigInfo) {
        userConfigFileProvider.provide(configInfo).onSuccess {
            if (!it.exists()) {
                writeDefault(configInfo)
            }
        }.onFailure { log.warn(it) }
    }

    override fun writeDefault(configInfo: ConfigInfo) {
        val file = defaultConfigFileProvider.provide(configInfo).getOrElse {
            log.warn(it) { "Can't access default config $configInfo. Looks like the plugin jar file is harmed" }
            return
        }
        file.read()
            .onSuccess { write(configInfo, it) }
            .onFailure { log.warn(it) }
    }
}
