package site.vie10.radio.config

/**
 * @author vie10
 **/
interface ConfigFile {

    val path: String

    fun read(): Result<ByteArray>

    fun write(byteArray: ByteArray)

    fun exists(): Boolean
}
