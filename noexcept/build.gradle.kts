plugins {
    java
    application
    id("io.freefair.aspectj.post-compile-weaving") version "8.13.1"
}


group = "org.ldemetrios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.aspectj:aspectjrt:1.9.23")
    testImplementation("junit:junit:4.13.2")
}

tasks.test {
    useJUnitPlatform()
}

sourceSets.main {
    java.srcDir("src")
//    aspectj.srcDir("aspect")
}

//configure<io.freefair.gradle.plugins.aspectj.AspectJCompileOptions> {
//    compilerArgs.add("-showWeaveInfo")
//}
//
//configure<io.freefair.gradle.plugins.aspectj.AspectJCompileOptions> {
//    compilerArgs.add("-showWeaveInfo")
//}