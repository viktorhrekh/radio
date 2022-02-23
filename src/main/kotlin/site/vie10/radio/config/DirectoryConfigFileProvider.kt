package site.vie10.radio.config

import java.io.File

/**
 * @author vie10
 **/
class DirectoryConfigFileProvider(
    private val directory: File
) : ConfigFileProvider {

    override fun provide(configInfo: ConfigInfo): Result<ConfigFile> = runCatching {
        if (configInfo.name.isEmpty()) {
            throw IllegalArgumentException("Unable provide config file with empty name")
        }
        val file = directory.resolve(configInfo.pathWithExtension)
        ConfigFileImpl(file)
    }

    private class ConfigFileImpl(private val file: File) : ConfigFile {

        override val path: String by lazy { file.path }

        override fun read(): Result<ByteArray> = runCatching { file.readBytes() }

        override fun write(byteArray: ByteArray) {
            file.apply {
                parentFile.apply {
                    if (!exists()) {
                        mkdirs()
                    }
                }

                writeBytes(byteArray)
            }
        }

        override fun exists(): Boolean = file.exists()

        override fun toString(): String {
            return "ConfigFile(path='$path')"
        }
    }
}
