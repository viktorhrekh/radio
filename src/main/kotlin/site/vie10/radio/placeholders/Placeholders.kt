package site.vie10.radio.placeholders

import site.vie10.radio.player.Player

/**
 * @author vie10
 **/

private const val PLACEHOLDER_FORMAT = "{%s}"

fun Player.applyPlaceholdersAndSendMessage(message: List<String>, vararg placeholders: Pair<String, Any>) {
    message.forEach { applyPlaceholdersAndSendMessage(it, *placeholders) }
}

fun Player.applyPlaceholdersAndSendMessage(message: String, vararg placeholders: Pair<String, Any>) {
    var temp = message.applyPlaceholders("target" to nickname)
    placeholders.forEach {
        temp = temp.applyPlaceholder(it)
    }
    sendMessage(temp)
}

fun String.applyPlaceholders(vararg placeholders: Pair<String, Any>): String {
    var temp = this
    placeholders.forEach {
        temp = temp.applyPlaceholder(it)
    }
    return temp
}

fun String.applyPlaceholder(placeholder: Pair<String, Any>): String {
    return replace(PLACEHOLDER_FORMAT.format(placeholder.first), placeholder.second.toString())
}
