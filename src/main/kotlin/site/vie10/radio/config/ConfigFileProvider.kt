package site.vie10.radio.config

/**
 * @author vie10
 **/
interface ConfigFileProvider {

    fun provide(configInfo: ConfigInfo): Result<ConfigFile>
}
