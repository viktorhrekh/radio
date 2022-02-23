package site.vie10.radio.player

/**
 * @author vie10
 **/
abstract class BasePlayer(
    final override val id: String,
    final override val nickname: String
) : Player {

    final override fun sendMessage(lines: List<String>) {
        lines.forEach { sendMessage(it) }
    }
}
