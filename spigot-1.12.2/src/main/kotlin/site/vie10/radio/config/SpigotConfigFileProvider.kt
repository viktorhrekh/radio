package site.vie10.radio.config

import org.bukkit.plugin.Plugin

/**
 * @author vie10
 **/
class SpigotConfigFileProvider(
    private val plugin: Plugin,
    private val directory: String
) : ConfigFileProvider {

    override fun provide(configInfo: ConfigInfo): Result<ConfigFile> = runCatching {
        val path = "$directory/${configInfo.pathWithExtension}"
        val content = plugin.getResource(path)?.use {
            it.readBytes()
        } ?: throw IllegalArgumentException("Config $configInfo not found")
        ImmutableConfigFile(path, content)
    }

    private class ImmutableConfigFile(
        override val path: String,
        private val byteArray: ByteArray
    ) : ConfigFile {

        override fun read(): Result<ByteArray> = runCatching { byteArray }

        override fun write(byteArray: ByteArray) {
            throw IllegalStateException("The config file is immutable")
        }

        override fun exists(): Boolean = true

        override fun toString(): String = "ConfigFile(path='$path')"
    }
}
