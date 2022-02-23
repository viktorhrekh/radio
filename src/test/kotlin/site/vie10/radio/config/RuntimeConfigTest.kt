package site.vie10.radio.config

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import site.vie10.radio.config.RuntimeConfig.removeFromRuntime
import site.vie10.radio.config.RuntimeConfig.runtimeVar
import site.vie10.radio.config.RuntimeConfig.uploadToRuntime

/**
 * @author vie10
 **/
class RuntimeConfigTest : BehaviorSpec({

    afterEach {
        RuntimeConfig.clean()
    }

    given("empty runtime config") {
        `when`("assigns value") {
            then("the same value is accessible") {
                val value = "some text"
                TestConfig::notNullName.runtimeVar = value

                TestConfig::notNullName.runtimeVar shouldBe value
            }
        }

        `when`("uploads object") {
            then("the object fields is accessible") {
                val config = TestConfig()
                config.uploadToRuntime()

                TestConfig::notNullName.runtimeVar shouldBe config.notNullName
            }
        }

        `when`("remove config") {
            then("does not throw") {
                shouldNotThrowAny {
                    TestConfig::class.removeFromRuntime()
                }
            }
        }

        `when`("invokes nullable value") {
            then("returns null") {
                TestConfig::nullableName.runtimeVar shouldBe null
            }
        }

        `when`("invokes not null value") {
            then("throws NullPointerException") {
                shouldThrow<NullPointerException> {
                    TestConfig::notNullName.runtimeVar
                }
            }
        }
    }

    given("runtime config with uploaded object") {
        `when`("removes uploaded object") {
            then("invoke not null value throws NullPointerException") {
                TestConfig().uploadToRuntime()
                TestConfig::class.removeFromRuntime()

                shouldThrow<NullPointerException> {
                    TestConfig::notNullName.runtimeVar
                }
            }
        }

        `when`("cleans") {
            then("invoke not null value throws NullPoinerException") {
                TestConfig().uploadToRuntime()
                RuntimeConfig.clean()

                shouldThrow<NullPointerException> {
                    TestConfig::notNullName.runtimeVar
                }
            }
        }
    }
}) {

    data class TestConfig(
        val nullableName: String? = "name",
        val notNullName: String = "name"
    )
}
