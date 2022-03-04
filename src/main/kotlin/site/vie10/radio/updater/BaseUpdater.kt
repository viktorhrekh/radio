package site.vie10.radio.updater

import site.vie10.radio.config.RuntimeConfig.runtimeVar

/**
 * @author vie10
 **/
abstract class BaseUpdater : Updater {

    abstract val currentVersion: String
    override val enabled: Boolean
        get() = UpdaterConfig::enabled.runtimeVar
    override val delay: Long
        get() = UpdaterConfig::delay.runtimeVar
    override val download: Boolean
        get() = UpdaterConfig::download.runtimeVar

    override suspend fun fetchAvailableUpdate(): Result<UpdateInfo> = runCatching {
        val updateInfo = fetchUpdateInfo().getOrThrow()
        if (updateInfo.version == currentVersion) throw NoUpdateAvailable()
        updateInfo
    }

    protected abstract suspend fun fetchUpdateInfo(): Result<UpdateInfo>
}
