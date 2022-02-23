package site.vie10.radio.config

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*

class ConfigWriterImplTest : BehaviorSpec({
    val configInfo = ConfigInfo(name = "config", className = "", group = "group")

    given("not existent config info") {
        `when`("writes default if not exists") {
            then("writes default content") {
                val defaultContent = "default content".toByteArray()
                val targetFile = mockk<ConfigFile>().apply {
                    every { exists() } returns false
                    every { write(any()) } just runs
                }
                val defaultConfigFile = mockk<ConfigFile>().apply {
                    every { read() } returns Result.success(defaultContent)
                }
                val defaultConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(defaultConfigFile)
                }
                val userConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(targetFile)
                }
                val instance = ConfigWriterImpl(userConfigFileProvider, defaultConfigFileProvider)

                instance.writeDefaultIfNotExists(configInfo)

                verify { targetFile.write(defaultContent) }
            }
        }

        `when`("writes default") {
            then("writes default content") {
                val defaultContent = "default content".toByteArray()
                val targetFile = mockk<ConfigFile>().apply {
                    every { exists() } returns false
                    every { write(any()) } just runs
                }
                val defaultConfigFile = mockk<ConfigFile>().apply {
                    every { read() } returns Result.success(defaultContent)
                }
                val defaultConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(defaultConfigFile)
                }
                val userConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(targetFile)
                }
                val instance = ConfigWriterImpl(userConfigFileProvider, defaultConfigFileProvider)

                instance.writeDefault(configInfo)

                verify { targetFile.write(defaultContent) }
            }
        }

        `when`("writes content") {
            then("writes the content") {
                val targetFile = mockk<ConfigFile>().apply {
                    every { exists() } returns false
                    every { write(any()) } just runs
                }
                val userConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(targetFile)
                }
                val instance = ConfigWriterImpl(userConfigFileProvider, mockk())
                val newContent = "new content".toByteArray()

                instance.write(configInfo, newContent)

                verify { targetFile.write(newContent) }
            }
        }
    }

    given("existent config info") {
        `when`("writes default if not exists") {
            then("doesn't rewrite previous content") {
                val defaultContent = "default content".toByteArray()
                val targetFile = mockk<ConfigFile>().apply {
                    every { exists() } returns true
                }
                val defaultConfigFile = mockk<ConfigFile>().apply {
                    every { read() } returns Result.success(defaultContent)
                }
                val defaultConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(defaultConfigFile)
                }
                val userConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(targetFile)
                }
                val instance = ConfigWriterImpl(userConfigFileProvider, defaultConfigFileProvider)

                instance.writeDefaultIfNotExists(configInfo)

                verify(exactly = 0) { targetFile.write(defaultContent) }
            }
        }

        `when`("writes default") {
            then("rewrites previous content") {
                val defaultContent = "default content".toByteArray()
                val targetFile = mockk<ConfigFile>().apply {
                    every { exists() } returns true
                    every { write(any()) } just runs
                }
                val defaultConfigFile = mockk<ConfigFile>().apply {
                    every { read() } returns Result.success(defaultContent)
                }
                val defaultConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(defaultConfigFile)
                }
                val userConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(targetFile)
                }
                val instance = ConfigWriterImpl(userConfigFileProvider, defaultConfigFileProvider)

                instance.writeDefault(configInfo)

                verify { targetFile.write(defaultContent) }
            }
        }

        `when`("writes content") {
            then("rewrites previous content") {
                val targetFile = mockk<ConfigFile>().apply {
                    every { exists() } returns true
                    every { write(any()) } just runs
                }
                val userConfigFileProvider = mockk<ConfigFileProvider>().apply {
                    every { provide(configInfo) } returns Result.success(targetFile)
                }
                val instance = ConfigWriterImpl(userConfigFileProvider, mockk())
                val newContent = "new content".toByteArray()

                instance.write(configInfo, newContent)

                verify { targetFile.write(newContent) }
            }
        }
    }
})
