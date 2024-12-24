plugins {
    kotlin("jvm") version "2.0.21"
    id("org.graalvm.buildtools.native") version "0.10.3"
}

group = "codemine"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(22)
}

graalvmNative {
    binaries {
        named("main") {
            mainClass.set("MainKt")
            imageName = "dir-checksums"
        }
    }
}
