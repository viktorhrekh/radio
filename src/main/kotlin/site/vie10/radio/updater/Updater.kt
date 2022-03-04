package site.vie10.radio.updater

import org.koin.core.component.KoinComponent

/**
 * @author vie10
 **/
interface Updater : KoinComponent {

    val enabled: Boolean
    val delay: Long
    val download: Boolean

    suspend fun fetchAvailableUpdate(): Result<UpdateInfo>

    suspend fun isUpdateDownloaded(updateInfo: UpdateInfo): Result<Boolean>

    suspend fun downloadUpdate(updateInfo: UpdateInfo): Result<Boolean>
}
