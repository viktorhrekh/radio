package site.vie10.radio.config

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.result.shouldBeFailureOfType
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainIgnoringCase
import java.io.File

class DirectoryConfigFileProviderTest : BehaviorSpec({

    given("not existent config info") {
        val tempDir = tempdir()

        `when`("provides") {
            then("write creates the config file") {
                val instance = DirectoryConfigFileProvider(tempDir)
                val configInfo = ConfigInfo("config", "", group = "group")
                instance.provide(configInfo).shouldBeSuccess {
                    it.write("".toByteArray())
                    tempDir.resolve(configInfo.pathWithExtension).exists() shouldBe true
                }
            }
        }
    }

    given("not existent directory") {
        val notExistentDirectory = File("some not existent directory")

        `when`("provides") {
            then("success") {
                val instance = DirectoryConfigFileProvider(notExistentDirectory)
                val configInfo = ConfigInfo("config", "")
                instance.provide(configInfo).shouldBeSuccess()
            }
        }
    }

    given("config info with empty name") {
        val configInfo = ConfigInfo("", "")

        `when`("provides") {
            then("failure") {
                val instance = DirectoryConfigFileProvider(File(""))
                instance.provide(configInfo).shouldBeFailureOfType<IllegalArgumentException>()
            }
        }
    }

    given("config info with name") {
        val configInfo = ConfigInfo("name", "group")

        `when`("provides") {
            then("config file path contains the name") {
                val instance = DirectoryConfigFileProvider(File(""))
                instance.provide(configInfo).shouldBeSuccess {
                    it.path shouldContainIgnoringCase configInfo.name
                }
            }
        }

        and("group") {
            `when`("provides") {
                then("config file path contains the name and group") {
                    val instance = DirectoryConfigFileProvider(File(""))
                    instance.provide(configInfo).shouldBeSuccess {
                        it.path shouldContainIgnoringCase configInfo.name
                        it.path shouldContainIgnoringCase configInfo.group
                    }
                }
            }
        }
    }

    given("existent config info") {
        val configInfo = ConfigInfo("test", "")
        val configDirectory = File(DirectoryConfigFileProviderTest::class.java.getResource("/config")!!.file)
        val instance = DirectoryConfigFileProvider(configDirectory)

        `when`("provides") {
            val result = instance.provide(configInfo)

            then("config file exists") {
                result.shouldBeSuccess {
                    it.exists() shouldBe true
                }
            }
            then("config file has content") {
                result.shouldBeSuccess {
                    it.read().shouldBeSuccess()
                }
            }
        }
    }
})
