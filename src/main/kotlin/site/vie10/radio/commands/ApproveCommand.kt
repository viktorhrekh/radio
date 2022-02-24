package site.vie10.radio.commands

import org.koin.core.component.inject
import site.vie10.radio.Radio
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.placeholders.applyPlaceholdersAndSendMessage
import site.vie10.radio.player.Player
import site.vie10.radio.storage.NotFoundException
import site.vie10.radio.storage.Storage
import site.vie10.radio.styles.StyleConfig
import site.vie10.radio.utils.commandMessages
import site.vie10.radio.utils.commonMessages

/**
 * @author vie10
 **/
class ApproveCommand : BaseCommand(
    CommandConfig::approve.runtimeVar,
    { commandMessages.approveHelp },
    { true },
    { true }
) {

    private val radio: Radio by inject()
    private val storage: Storage by inject()
    private val List<String>.styleName: String
        get() = this[1]
    private val List<String>.id: String
        get() = this[0]

    override fun onExecute(player: Player, args: List<String>) {
        val styleName = args.styleName
        val style = StyleConfig::styles.runtimeVar.getOrElse(styleName) {
            player.applyPlaceholdersAndSendMessage(commonMessages.styleNotExists, "style" to styleName)
            return
        }

        val id: String = args.id
        runCatching {
            val suggestion = storage.remSuggestion(id).getOrThrow()
            radio.broadcast(style, suggestion.content)
            player.applyPlaceholdersAndSendMessage(
                commonMessages.approved,
                "id" to id,
                "from" to suggestion.from,
                "created" to suggestion.formattedCreated,
                "content" to suggestion.content
            )
        }.onFailure {
            if (it is NotFoundException) {
                player.applyPlaceholdersAndSendMessage(commonMessages.suggestionNotFound, "id" to id)
                return
            }
            throw it
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
