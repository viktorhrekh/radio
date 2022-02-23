package site.vie10.radio.logging

import org.koin.core.component.KoinComponent

/**
 * @author vie10
 **/
interface Logger : KoinComponent {

    fun info(block: () -> Any)

    fun warn(throwable: Throwable)

    fun warn(throwable: Throwable? = null, block: () -> Any)

    fun exception(throwable: Throwable, block: () -> Any)
}
