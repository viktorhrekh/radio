package site.vie10.radio

import org.koin.core.component.inject
import site.vie10.radio.server.Server

/**
 * @author vie10
 **/
class ServerRadio : BaseRadio() {

    private val server: Server by inject()

    override fun broadcastStyled(input: List<String>) {
        server.broadcast(input)
    }
}
