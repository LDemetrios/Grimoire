import org.gradle.kotlin.dsl.main
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.0-RC"
}

group = "org.ldemetrios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(files("${project.rootDir}/prolog/lib/2p-4.0.3.jar"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
sourceSets.main {
    java.srcDir("src")
}

sourceSets.test {
    java.srcDir("test")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.addAll("-Xmulti-dollar-interpolation", "-Xcontext-parameters")
}