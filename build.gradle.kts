import java.util.concurrent.TimeUnit

plugins {
    id("java")
}

group = "pit.stop.tests"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.appium:java-client:9.3.0")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.25.0")
    testImplementation("org.testng:testng:7.10.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("io.qameta.allure:allure-testng:2.29.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Removes files older than 3 days from artifact/result folders so they don't pile up.
val cleanOldArtifacts by tasks.registering {
    doLast {
        val cutoff: Long = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3)
        val targets = listOf("build/test-artifacts", "build/test-results")
        var deleted = 0
        targets.forEach { dir ->
            val root = file(dir)
            if (root.exists()) {
                root.walk()
                    .filter { it.isFile && it.lastModified() < cutoff }
                    .forEach {
                        if (it.delete()) deleted++
                    }
            }
        }
        if (deleted > 0) println("[cleanOldArtifacts] removed $deleted file(s) older than 3 days")
    }
}

// Стандартные настройки, общие для всех Test-тасок (полный прогон и сплит на 2 части).
fun org.gradle.api.tasks.testing.Test.applyStandardSetup() {
    dependsOn(cleanOldArtifacts)
    systemProperties = System.getProperties()
        .entries
        .associate { it.key.toString() to it.value }
        .toMutableMap()
    testLogging {
        events("passed", "failed", "skipped", "standardOut", "standardError")
        showStandardStreams = true
        showExceptions = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

// Полный прогон (~2 часа). На длинной дистанции UiAutomator2 имеет тенденцию падать —
// если устойчивость важнее покрытия, используй testPart1 / testPart2 с холодным
// ребутом эмулятора между ними.
tasks.test {
    applyStandardSetup()
    useTestNG {
        suiteXmlFiles = listOf(file("src/test/resources/suites/android.xml"))
    }
}

// Часть 1A: фундаментальные флоу + Settings + Profile + Kazakh.
// ~25 минут, 34 кейса.
tasks.register<Test>("testPart1a") {
    description = "Foundational + Settings + Profile + Kazakh (~25 min, 34 cases)"
    group = "verification"
    applyStandardSetup()
    useTestNG {
        suiteXmlFiles = listOf(file("src/test/resources/suites/androidPart1a.xml"))
    }
}

// Часть 1B: Invite + Rules-флоу + BACK-навигация.
// ~20 минут, 27 кейсов.
tasks.register<Test>("testPart1b") {
    description = "Invite + Rules flow + BACK navigation (~20 min, 27 cases)"
    group = "verification"
    applyStandardSetup()
    useTestNG {
        suiteXmlFiles = listOf(file("src/test/resources/suites/androidPart1b.xml"))
    }
}

// Часть 2: Testing-флоу + Советы + тяжёлые экраны (Article, Question).
// ~25-30 минут, 30 кейсов, больше ANR — рекомендуется холодный ребут эмулятора
// между каждой парой частей (1A → 1B → 2).
tasks.register<Test>("testPart2") {
    description = "Testing flow + Advices + heavy screens (~30 min, 30 cases)"
    group = "verification"
    applyStandardSetup()
    useTestNG {
        suiteXmlFiles = listOf(file("src/test/resources/suites/androidPart2.xml"))
    }
}
