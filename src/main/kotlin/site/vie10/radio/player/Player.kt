package site.vie10.radio.player

import org.koin.core.component.KoinComponent

/**
 * @author vie10
 **/
interface Player : KoinComponent {

    val nickname: String
    val id: String

    fun sendMessage(lines: List<String>)

    fun sendMessage(text: String)

    fun hasPermission(permission: String): Boolean
}
