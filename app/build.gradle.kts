import kr.entree.spigradle.kotlin.*

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("kapt") version "1.6.0"
    id("kr.entree.spigradle") version "2.2.4"
}

group = "com.gmail.noxdawn"
version = "0.0.1"

spigot {
    main = "com.gmail.noxdawn.Plugin"
    description = "A sample plugin"
    load = kr.entree.spigradle.data.Load.STARTUP
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(spigot("1.17.1"))
}

tasks.register("populate plugin", Copy::class) {
    group = "debug"
    dependsOn("build")
    from("build/libs")
    into("server/plugins")
}
