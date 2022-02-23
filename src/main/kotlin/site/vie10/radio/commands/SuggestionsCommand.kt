package site.vie10.radio.commands

import org.koin.core.component.inject
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.storage.Storage
import site.vie10.radio.styles.StyleConfig
import site.vie10.radio.utils.commandMessages
import site.vie10.radio.utils.commonMessages

/**
 * @author vie10
 **/
class SuggestionsCommand : BaseCommand(
    CommandConfig::suggestions.runtimeVar,
    { commandMessages.suggestionsHelp },
) {

    private val storage: Storage by inject()

    override fun onExecute(player: Player, args: List<String>) {
        val suggestions = storage.getSuggestions().getOrThrow()
        if (suggestions.isEmpty()) {
            player.applyPlaceholdersAndSendMessage(commonMessages.noSuggestions)
            return
        }
        suggestions.forEach {
            player.applyPlaceholdersAndSendMessage(
                commonMessages.suggestion,
                "id" to it.id,
                "content" to it.content,
                "from" to it.from,
                "created" to it.formattedCreated
            )
        }
    }

    override fun onTabComplete(player: Player, args: List<String>): List<String> {
        return when (args.size) {
            1 -> storage.getSuggestions().getOrThrow().map { it.id }
            2 -> StyleConfig::styles.runtimeVar.map { it.key }
            else -> emptyList()
        }
    }
}
