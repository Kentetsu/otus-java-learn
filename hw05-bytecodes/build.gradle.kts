import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("org.ow2.asm:asm-commons")
    implementation ("ch.qos.logback:logback-classic")
}

tasks {
    create<ShadowJar>("TestLoggingJar") {
        archiveBaseName.set("TestLogging")
        archiveVersion.set("")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.eon.TestLogging",
                "Premain-Class" to "ru.eon.Agent"))
        }
        from(sourceSets.main.get().output)
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }
    build {
        dependsOn("TestLoggingJar")
    }
}