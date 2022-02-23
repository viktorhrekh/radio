package site.vie10.radio.storage

import org.koin.core.component.KoinComponent
import site.vie10.radio.suggestions.Suggestion

/**
 * @author vie10
 **/
interface Storage : KoinComponent {

    fun remSuggestion(id: String): Result<Suggestion>

    fun saveSuggestion(suggestion: Suggestion): Result<Unit>

    fun getSuggestionsRelatedTo(from: String): Result<List<Suggestion>>

    fun findSuggestionWithId(id: String): Result<Suggestion>

    fun getSuggestions(): Result<List<Suggestion>>
}
