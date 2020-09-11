import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(kotlin("gradle-plugin", version = "1.3.72"))
  }
}

repositories {
  mavenCentral()
}

plugins{
  kotlin("jvm") version "1.3.72"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.4"
}