package site.vie10.radio.gui

/**
 * @author vie10
 **/
abstract class BaseViewData : ViewData {

    final override fun applyPlaceholders(input: List<String>): List<String> = input.map { applyPlaceholders(it) }
}
