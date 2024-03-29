package site.vie10.radio

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.fileProperties
import site.vie10.radio.commands.RadioCommandExecutor
import site.vie10.radio.di.Module

/**
 * @author vie10
 **/
@DelicateCoroutinesApi
@Suppress("unused")
class RadioSpigotAdapter : JavaPlugin(), KoinComponent {

    init {
        startKoin {
            runCatching { fileProperties("/radio.properties") }
            modules(Module.release, Module.plugin(this@RadioSpigotAdapter))
        }
    }

    private val metrics: Metrics = get()
    private val plugin: RadioPlugin by inject()
    private val radioCommandExecutor: RadioCommandExecutor by inject()

    override fun onEnable() {
        plugin.scope.launch { plugin.start() }
        getCommand("radio")?.apply {
            @Suppress("UsePropertyAccessSyntax")
            setExecutor(radioCommandExecutor) // Use property access for support 1.14+.
            tabCompleter = radioCommandExecutor
        }
    }

    override fun reloadConfig() {
        plugin.scope.launch { plugin.reloadConfig() }
    }

    override fun onDisable() {
        plugin.scope.launch { plugin.stop() }
    }
}
