package site.vie10.radio.commands

import org.koin.core.component.inject
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.storage.NotFoundException
import site.vie10.radio.storage.Storage
import site.vie10.radio.utils.commandMessages
import site.vie10.radio.utils.commonMessages

/**
 * @author vie10
 **/
class DeclineCommand : BaseCommand(
    CommandConfig::decline.runtimeVar,
    { commandMessages.declineHelp },
    { true },
) {

    private val storage: Storage by inject()
    private val List<String>.id: String
        get() = this[0]

    override fun onExecute(player: Player, args: List<String>) {
        val id: String = args.id
        runCatching {
            val suggestion = storage.remSuggestion(id).getOrThrow()
            player.applyPlaceholdersAndSendMessage(
                commonMessages.declined,
                "id" to id,
                "from" to suggestion.from,
                "created" to suggestion.formattedCreated,
                "content" to suggestion.content
            )
        }.onFailure {
            if (it is NotFoundException) {
                player.applyPlaceholdersAndSendMessage(commonMessages.suggestionNotFound, "id" to id)
            }
            throw it
        }
    }

    override fun onTabComplete(player: Player, args: List<String>): List<String> {
        return when (args.size) {
            1 -> storage.getSuggestions().getOrThrow().map { it.id }
            else -> emptyList()
        }
    }
}
