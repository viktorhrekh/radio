package site.vie10.radio.config.hoplite

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import site.vie10.radio.config.ConfigFile
import site.vie10.radio.config.ConfigFileProvider
import site.vie10.radio.config.ConfigInfo
import site.vie10.radio.config.DirectoryConfigFileProvider
import java.io.File
import kotlin.reflect.jvm.jvmName

/**
 * @author vie10
 **/
class HopliteConfigLoaderTest : BehaviorSpec({

    given("not existent config info") {
        `when`("loads") {
            then("failure") {
                val notExistentFolder = File("some not exists folder")
                val fileProvider = DirectoryConfigFileProvider(notExistentFolder)
                val instance = HopliteConfigLoader(fileProvider)
                val notExistentConfigInfo = ConfigInfo("some not existent config info", "")

                instance.load(notExistentConfigInfo).shouldBeFailure()
            }
        }
    }

    given("existent config info") {
        val existentConfigInfo = ConfigInfo("name", TestConfig::class.jvmName)

        and("config file without optional field") {
            `when`("loads") {
                val configFile = mockk<ConfigFile>()
                every { configFile.read() } returns Result.success("some: some".toByteArray())
                val fileProvider = mockk<ConfigFileProvider>()
                every { fileProvider.provide(existentConfigInfo) } returns Result.success(configFile)
                val instance = HopliteConfigLoader(fileProvider)

                then("success") {
                    instance.load(existentConfigInfo).shouldBeSuccess()
                }

                then("assigns default value") {
                    instance.load(existentConfigInfo).shouldBeSuccess {
                        (it as TestConfig).name shouldBe TestConfig.DEFAULT_NAME
                    }
                }
            }
        }

        `when`("loads") {
            then("success") {
                val configFile = mockk<ConfigFile>()
                every { configFile.read() } returns Result.success("name: name".toByteArray())
                val fileProvider = mockk<ConfigFileProvider>()
                every { fileProvider.provide(existentConfigInfo) } returns Result.success(configFile)
                val instance = HopliteConfigLoader(fileProvider)

                instance.load(existentConfigInfo).shouldBeSuccess()
            }
        }
    }
}) {

    data class TestConfig(val name: String = DEFAULT_NAME) {
        companion object {
            const val DEFAULT_NAME = "default name"
        }
    }
}
