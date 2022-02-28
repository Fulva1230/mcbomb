import kr.entree.spigradle.kotlin.*

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    id("kr.entree.spigradle") version "2.3.4"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.gmail.noxdawn"
version = "0.0.1"
val koin_version = "3.1.5"

spigot {
    main = "com.gmail.noxdawn.Plugin"
    description = "A plugin making explosion funny"
    load = kr.entree.spigradle.data.Load.STARTUP
    apiVersion = "1.18"

    commands {
        create("bomb") {
            description = "Make the items hold bombs"
            usage = "/<command> [last] [power]"
        }
        create("bombinfo") {
            description = "Check items hold are bombs"
            usage = "/<command>"
        }
        create("bombremote") {
            description = "Make the items hold bomb triggers"
            usage = "/<command> <label>"
        }
        create("remotebomb"){
            description = "Make the items hold remote bombs"
            usage = "/<command> <label> [power]"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-core:$koin_version")
    compileOnly(spigot("1.18.1"))
}

tasks.register("populate-plugin", Copy::class) {
    group = "debug"
    dependsOn("shadowJar")
    from("build/libs") {
        include("*-all.jar")
    }
    into("server/plugins")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_17
}
