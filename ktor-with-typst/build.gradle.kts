plugins {
    kotlin("jvm")
}

group = "org.ldemetrios"
version = "1.0-SNAPSHOT"

sourceSets.main {
    java.srcDir("src")
    resources.srcDir("pages")
}

sourceSets.test {
    java.srcDir("test")
}

dependencies {
    implementation("org.ldemetrios:tyko:0.4.0")
    implementation("net.java.dev.jna:jna:5.13.0")

    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-server-netty:2.3.5")
    implementation("io.ktor:ktor-server-html-builder:2.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.9.1")
}