package site.vie10.radio.logger

import site.vie10.radio.logging.BaseLogger
import java.util.logging.Level
import java.util.logging.Logger as JavaLogger

/**
 * @author vie10
 **/
class LoggerAdapter(
    private val logger: JavaLogger
) : BaseLogger() {

    override fun info(message: String) {
        logger.log(Level.INFO, message)
    }

    override fun warn(message: String, throwable: Throwable?) {
        logger.log(Level.WARNING, message, throwable)
    }

    override fun exception(message: String, throwable: Throwable) {
        logger.log(Level.SEVERE, message, throwable)
    }
}
