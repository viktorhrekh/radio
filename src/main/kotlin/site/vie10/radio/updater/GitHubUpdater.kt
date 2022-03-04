package site.vie10.radio.updater

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import site.vie10.radio.updater.github.ReleaseInfo
import java.io.File
import java.io.OutputStream
import java.net.URL

/**
 * @author vie10
 **/
class GitHubUpdater(
    currentVersion: String,
    repositoryHolder: String,
    repositoryId: String,
    workDir: String
) : BaseUpdater() {

    override val currentVersion: String = "v$currentVersion"
    private val releasesApiURL = URL("https://api.github.com/repos/$repositoryHolder/$repositoryId/releases")
    private val workDir = File(workDir)
    private val ReleaseInfo.asUpdateInfo: UpdateInfo
        get() = UpdateInfo(tagName, body, distributionAsset.downloadUrl, distributionAsset.name)
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun fetchUpdateInfo(): Result<UpdateInfo> = runCatching {
        withContext(Dispatchers.IO) {
            releasesApiURL.openConnection().apply {
                setRequestProperty("Accept", "application/vnd.github.v3+json")
            }.getInputStream().use {
                val jsonText = it.reader().readText()
                val releases: Set<ReleaseInfo> = json.decodeFromString(jsonText)
                releases.first()
            }
        }.asUpdateInfo
    }

    override suspend fun isUpdateDownloaded(updateInfo: UpdateInfo): Result<Boolean> = runCatching {
        workDir.resolve(updateInfo.fileName).exists()
    }

    override suspend fun downloadUpdate(updateInfo: UpdateInfo): Result<Boolean> = runCatching {
        val file = workDir.resolve(updateInfo.fileName)
        if (file.exists()) return@runCatching true
        withContext(Dispatchers.IO) {
            workDir.apply { if (!exists()) mkdirs() }
            file.apply { if (!exists()) createNewFile() }
            file.outputStream().use {
                URL(updateInfo.downloadUrl).downloadTo(it)
            }
        }
        true
    }

    private suspend fun URL.downloadTo(out: OutputStream) {
        withContext(Dispatchers.IO) {
            openConnection().getInputStream().use { `in` ->
                `in`.copyTo(out, 16 * 1024)
            }
        }
    }
}
