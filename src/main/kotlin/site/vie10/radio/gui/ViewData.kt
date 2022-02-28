package site.vie10.radio.gui

/**
 * @author vie10
 **/
interface ViewData {

    fun applyPlaceholders(input: List<String>): List<String>

    fun applyPlaceholders(input: String): String
}
