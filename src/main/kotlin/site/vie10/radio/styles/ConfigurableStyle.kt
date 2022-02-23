package site.vie10.radio.styles

import site.vie10.radio.placeholders.applyPlaceholders
import site.vie10.radio.utils.currentDate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author vie10
 **/
data class ConfigurableStyle(
    val permission: String,
    val format: List<String>,
    val lineFormat: String,
    val lineWraps: LineWraps,
    val dateFormat: String,
    val timeFormat: String,
) : Style {

    private val dateFormatter: DateFormat = SimpleDateFormat(dateFormat)
    private val timeFormatter: DateFormat = SimpleDateFormat(timeFormat)

    override fun format(input: String): List<String> {
        val lines = lineWraps.applyIfEnabled(input).joinToString(System.lineSeparator()) {
            lineFormat.applyPlaceholders(
                "content" to it
            )
        }
        return format.map {
            val currentDate = currentDate
            it.applyPlaceholders(
                "date" to dateFormatter.format(currentDate),
                "time" to timeFormatter.format(currentDate),
                "lines" to lines
            )
        }
    }

    data class LineWraps(
        val enabled: Boolean,
        val length: Int,
    ) {

        fun applyIfEnabled(text: String): List<String> = if (enabled) {
            apply(text)
        } else listOf(text)

        private fun apply(text: String): List<String> {
            if (text.length <= length) return listOf(text)
            val count = text.length / length
            val result: MutableList<String> = LinkedList()
            repeat(count) {
                val n = it + 1
                val toIndex = n * length
                result.add(text.substring(toIndex - length, toIndex))
            }
            val summary = count * length
            if (summary < text.length) {
                result.add(text.substring(summary, text.lastIndex))
            }
            return result
        }
    }
}
