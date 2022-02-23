package site.vie10.radio

import kotlinx.coroutines.*
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import site.vie10.radio.commands.*
import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.ConfigLoader
import site.vie10.radio.config.ConfigWriter
import site.vie10.radio.config.RuntimeConfig
import site.vie10.radio.config.RuntimeConfig.uploadToRuntime
import site.vie10.radio.logging.Logger
import site.vie10.radio.logging.wrappedRunNoResult
import site.vie10.radio.messages.MessageConfig
import site.vie10.radio.server.Server
import site.vie10.radio.styles.StyleConfig
import site.vie10.radio.suggestions.SuggestionConfig

/**
 * @author vie10
 **/
class RadioPluginImpl : RadioPlugin {

    override val scope: CoroutineScope by inject(named(RadioPlugin.COROUTINE_SCOPE_NAME))

    private val configInfo: Set<ConfigInfo> by lazy {
        setOf(MessageConfig.Info, CommandConfig.Info, SuggestionConfig.Info, StyleConfig.Info)
    }
    private val commands: Set<Command> by lazy {
        setOf(
            ApproveCommand(),
            DeclineCommand(),
            BroadcastCommand(),
            StylesCommand(),
            SuggestCommand(),
            SuggestionsCommand(),
            ReloadCommand()
        )
    }
    private val log: Logger by inject()
    private val configLoader: ConfigLoader by inject()
    private val configWriter: ConfigWriter by inject()
    private val server: Server by inject()

    override suspend fun start() = wrappedRunNoResult(
        logging = {
            onStart = {
                log.info { "Starting plugin.." }
            }
            onSuccess = { measuredMillis, _ ->
                log.info { "Plugin successfully started in $measuredMillis millis." }
            }
            onFailure = {
                log.exception(it) { "Starting plugin failed by unexpected exception." }
            }
        }
    ) {
        reloadConfig()
        registerCommands()
    }

    override suspend fun stop() = wrappedRunNoResult(
        logging = {
            onStart = {
                log.info { "Stopping plugin.." }
            }
            onSuccess = { measuredMillis, _ ->
                log.info { "Plugin successfully stopped in $measuredMillis millis." }
            }
            onFailure = {
                log.exception(it) { "Stopping plugin failed by unexpected exception." }
            }
        }
    ) {
        unregisterCommands()
        RuntimeConfig.clean()
    }

    private fun registerCommands() {
        commands.forEach { server.registerCommand(it) }
    }

    private fun unregisterCommands() {
        commands.forEach { server.unregisterCommand(it) }
    }

    override suspend fun reloadConfig() = wrappedRunNoResult(
        logging = {
            onStart = {
                log.info { "Reloading config.." }
            }
            onSuccess = { measuredMillis, _ ->
                log.info { "Config successfully reloaded in $measuredMillis millis." }
            }
            onFailure = {
                log.exception(it) { "Reloading config failed by unexpected exception." }
            }
        }
    ) {
        saveDefaultConfigIfNotExists()
        loadConfig()
    }

    private fun loadConfig() {
        runBlocking {
            for (info in configInfo) {
                async {
                    withContext(Dispatchers.IO) { configLoader.load(info) }.onSuccess {
                        it.uploadToRuntime()
                    }.onFailure {
                        log.warn(it) { "Loading config $info failed by unexpected exception." }
                    }
                }.start()
            }
        }
    }

    private fun saveDefaultConfigIfNotExists() {
        runBlocking {
            for (info in configInfo) {
                async {
                    withContext(Dispatchers.IO) { configWriter.writeDefaultIfNotExists(info) }
                }.start()
            }
        }
    }
}
