package site.vie10.radio.config.hoplite

import com.sksamuel.hoplite.addStreamSource
import site.vie10.radio.config.ConfigFileProvider
import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigLoader
import com.sksamuel.hoplite.ConfigLoader as SkHopliteConfigLoader

/**
 * @author vie10
 **/
class HopliteConfigLoader(
    private val configFileProvider: ConfigFileProvider
) : ConfigLoader {

    override fun load(configInfo: ConfigInfo): Result<Any> = runCatching {
        val configFile = configFileProvider.provide(configInfo).getOrThrow()
        val content = configFile.read().getOrThrow()
        SkHopliteConfigLoader.Builder()
            .withClassLoader(HopliteConfigLoader::class.java.classLoader)
            .addStreamSource(content.inputStream(), configInfo.extension)
            .build()
            .loadConfigOrThrow(configInfo.clazz, emptyList())
    }
}
