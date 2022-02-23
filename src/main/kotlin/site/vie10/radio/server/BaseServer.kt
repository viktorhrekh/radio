package site.vie10.radio.server

/**
 * @author vie10
 **/
abstract class BaseServer : Server {

    final override fun broadcast(input: List<String>) {
        input.forEach { broadcast(it) }
    }
}
