package site.vie10.radio.commands

import org.koin.core.component.inject
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.gui.BaseViewData
import site.vie10.radio.gui.GUIConfig
import site.vie10.radio.gui.GUIFactory
import site.vie10.radio.placeholders.applyPlaceholders
import site.vie10.radio.player.Player
import site.vie10.radio.storage.Storage
import site.vie10.radio.suggestions.Suggestion
import site.vie10.radio.utils.commandMessages

/**
 * @author vie10
 **/
class GUICommand : BaseCommand(
    CommandConfig::gui.runtimeVar,
    { commandMessages.guiHelp }
) {

    private val storage: Storage by inject()
    private val guiFactory: GUIFactory by inject()

    override fun onExecute(player: Player, args: List<String>) {
        val viewData = storage.getSuggestions().getOrThrow().map { SuggestionViewData(it) }
        val gui = guiFactory.create(GUIConfig::templates.runtimeVar["main"]!!, viewData)
        gui.render()
        gui.showFor(player)
    }

    private class SuggestionViewData(
        private val suggestion: Suggestion
    ) : BaseViewData() {

        override fun applyPlaceholders(input: String): String = input.applyPlaceholders(
            "id" to suggestion.id,
            "from" to suggestion.from,
            "content" to suggestion.content,
            "created" to suggestion.formattedCreated
        )
    }
}
