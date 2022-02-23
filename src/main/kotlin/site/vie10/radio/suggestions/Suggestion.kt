package site.vie10.radio.suggestions

import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.utils.asDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author vie10
 **/
data class Suggestion(
    val id: String = UUID.randomUUID().toString().substringBefore("-"),
    val from: String,
    val content: String,
    val createdUnixTimestamp: Long,
) {

    val formattedCreated: String
        get() = SimpleDateFormat(SuggestionConfig::dateTimeFormat.runtimeVar).format(createdUnixTimestamp.asDate)
}
