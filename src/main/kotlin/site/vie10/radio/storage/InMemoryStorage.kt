package site.vie10.radio.storage

import site.vie10.radio.suggestions.Suggestion
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author vie10
 **/
class InMemoryStorage : Storage {
    private val suggestionsMap: MutableMap<String, MutableList<Suggestion>> = ConcurrentHashMap()

    override fun remSuggestion(id: String): Result<Suggestion> = runCatching {
        val suggestion = findSuggestionWithId(id).getOrThrow()
        suggestionsMap.values.forEach {
            it.remove(suggestion)
        }
        suggestion
    }

    override fun findSuggestionWithId(id: String): Result<Suggestion> = runCatching {
        suggestionsMap.values.forEach {
            val result = it.firstOrNull { suggestion -> suggestion.id == id }
            if (result != null) {
                return@runCatching result
            }
        }
        throw NotFoundException()
    }

    override fun saveSuggestion(suggestion: Suggestion): Result<Unit> = runCatching {
        val suggestionsRelatedToFrom = suggestionsMap.computeIfAbsent(suggestion.from) { arrayListOf() }
        suggestionsRelatedToFrom.add(suggestion)
    }

    override fun getSuggestionsRelatedTo(from: String): Result<List<Suggestion>> = runCatching {
        if (!suggestionsMap.containsKey(from)) {
            emptyList()
        } else suggestionsMap[from]!!
    }
    
    override fun getSuggestions(): Result<List<Suggestion>> = runCatching {
        LinkedList<Suggestion>().apply {
            suggestionsMap.forEach { (_, suggestions) -> addAll(suggestions) }
        }
    }
}
