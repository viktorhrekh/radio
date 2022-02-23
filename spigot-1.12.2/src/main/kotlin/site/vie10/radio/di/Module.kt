package site.vie10.radio.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import org.bukkit.plugin.Plugin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module
import site.vie10.radio.Radio
import site.vie10.radio.RadioPlugin
import site.vie10.radio.RadioPluginImpl
import site.vie10.radio.ServerRadio
import site.vie10.radio.commands.RadioCommandExecutor
import site.vie10.radio.config.*
import site.vie10.radio.config.hoplite.HopliteConfigLoader
import site.vie10.radio.logger.LoggerAdapter
import site.vie10.radio.logging.Logger
import site.vie10.radio.server.Server
import site.vie10.radio.server.ServerAdapter
import site.vie10.radio.storage.InMemoryStorage
import site.vie10.radio.storage.Storage
import java.io.File
import org.koin.core.module.Module as KoinModule

/**
 * @author vie10
 **/
@DelicateCoroutinesApi
object Module {

    private val Scope.radioPluginScopeThreadsCount: Int
        get() = getProperty("plugin_scope_threads_count", Runtime.getRuntime().availableProcessors())
    private val Scope.jarConfigDirectory: String
        get() = getProperty("jar_config_directory", "config")
    private val Scope.userConfigDirectory: String
        get() = getProperty("user_config_directory", "plugins/radio")

    val release: KoinModule
        get() = module {
            single { InMemoryStorage() } bind Storage::class
            single { ServerRadio() } bind Radio::class
            single { LoggerAdapter(get<Plugin>().logger) } bind Logger::class
            single { ServerAdapter(get<Plugin>().server) } bind Server::class
            single { RadioCommandExecutor() }
            single { RadioPluginImpl() } bind RadioPlugin::class
            single(named(ConfigSource.Jar)) {
                SpigotConfigFileProvider(
                    get(), jarConfigDirectory
                )
            } bind ConfigFileProvider::class
            single(named(ConfigSource.User)) {
                DirectoryConfigFileProvider(
                    File(userConfigDirectory)
                )
            } bind ConfigFileProvider::class
            single {
                ConfigWriterImpl(
                    get(named(ConfigSource.User)), get(named(ConfigSource.Jar))
                )
            } bind ConfigWriter::class
            single { HopliteConfigLoader(get(named(ConfigSource.User))) } bind ConfigLoader::class
            single(named(RadioPlugin.COROUTINE_SCOPE_NAME)) {
                val context = newFixedThreadPoolContext(radioPluginScopeThreadsCount, RadioPlugin.COROUTINE_SCOPE_NAME)
                CoroutineScope(context)
            }
        }

    fun plugin(plugin: Plugin): KoinModule = module {
        single { plugin }
    }
}
