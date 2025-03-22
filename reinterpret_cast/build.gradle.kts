plugins {
    id("java")
}

group = "org.ldemetrios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

sourceSets.main {
    java.srcDir("src")
}

