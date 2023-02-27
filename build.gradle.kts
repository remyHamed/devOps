val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	application
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.serialization") version "1.6.21"
	id("org.jetbrains.dokka") version "1.6.21"
	id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "fr.imacaron"
version = "2.0"

application {
	mainClass.set("fr.imacaron.motrelou.ApplicationKt")
	val isDevelopment: Boolean = project.ext.has("developement")
	applicationDefaultJvmArgs = listOf("-Dio.ktor.developmebt=$isDevelopment")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.apache.commons:commons-dbcp2:2.9.0")
	implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-cors:$ktor_version")
	implementation("io.ktor:ktor-server-call-logging:$ktor_version")
	implementation("io.ktor:ktor-server-resources:$ktor_version")
	implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")
	implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")
	implementation("ch.qos.logback:logback-classic:$logback_version")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

	testImplementation(kotlin("test"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
//	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

java{
	manifest{
		attributes(mapOf("Main-Class" to "fr.imacaron.motrelou.ApplicationKt"))
	}
}

tasks.withType<Jar>{
	manifest{
		attributes["Main-Class"] = "fr.imacaron.motrelou.ApplicationKt"
	}
}

tasks.dokkaHtml.configure{
	outputDirectory.set(File("${buildDir.parentFile.absolutePath}/docs"))
}

tasks{
	shadowJar{
		manifest{
			attributes("Main-Class" to "fr.imacaron.motrelou.ApplicationKt")
		}
	}
}

tasks.test{
	useJUnitPlatform()
}