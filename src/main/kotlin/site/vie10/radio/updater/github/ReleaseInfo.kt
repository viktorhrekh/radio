package site.vie10.radio.updater.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val DISTRIBUTION_CONTENT_TYPE = "application/java-archive"

/**
 * @author vie10
 **/
@Serializable
data class ReleaseInfo(
    @SerialName("url")
    val url: String,
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("body")
    val body: String,
    @SerialName("assets")
    val assets: Set<Asset>
) {

    val distributionAsset: Asset by lazy {
        assets.first { it.contentType == DISTRIBUTION_CONTENT_TYPE }
    }

    @Serializable
    data class Asset(
        @SerialName("name")
        val name: String,
        @SerialName("content_type")
        val contentType: String,
        @SerialName("browser_download_url")
        val downloadUrl: String
    )
}
