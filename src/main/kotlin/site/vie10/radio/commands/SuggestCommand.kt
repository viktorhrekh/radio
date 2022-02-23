package site.vie10.radio.commands

import org.koin.core.component.inject
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.storage.Storage
import site.vie10.radio.suggestions.Suggestion
import site.vie10.radio.suggestions.SuggestionConfig
import site.vie10.radio.utils.commandMessages
import site.vie10.radio.utils.commonMessages
import site.vie10.radio.utils.nowUnixTimestamp

/**
 * @author vie10
 **/
class SuggestCommand : BaseCommand(
    CommandConfig::suggest.runtimeVar,
    { commandMessages.suggestHelp },
    { true }
) {

    private val storage: Storage by inject()

    override fun onExecute(player: Player, args: List<String>) {
        val suggestionsRelatedToPlayer = storage.getSuggestionsRelatedTo(player.id).getOrThrow()
        val maxSuggestionsPerPlayer = SuggestionConfig::maxPerPlayer.runtimeVar
        if (suggestionsRelatedToPlayer.size >= maxSuggestionsPerPlayer) {
            player.applyPlaceholdersAndSendMessage(
                commonMessages.youHaveReachedMaxSuggestions,
                "max-count" to maxSuggestionsPerPlayer
            )
            return
        }
        val content = args.joinToString(" ")
        val suggestion = Suggestion(from = player.id, content = content, createdUnixTimestamp = nowUnixTimestamp)
        storage.saveSuggestion(suggestion).getOrThrow()
        player.applyPlaceholdersAndSendMessage(commonMessages.suggestionSent)
    }
}
