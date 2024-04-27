import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Target
import org.jooq.meta.jaxb.*

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.netflix.dgs.codegen") version "6.0.3"
    id("org.jooq.jooq-codegen-gradle") version "3.19.7"
    id("application")
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

group = "com.trip_jr"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("com.trip_jr.tripJr.TripJrApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.postgresql:postgresql:42.6.2")
        classpath("org.jooq:jooq-codegen:3.19.7")
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springAiVersion"] = "0.8.1"
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.mindrot:jbcrypt:0.4")

    //TODO configure liquibase
//    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql:42.6.2")
    implementation("org.jooq:jooq:3.19.7")
    implementation("org.jooq:jooq-meta:3.19.7")
    implementation("org.jooq:jooq-codegen:3.19.7")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql:42.6.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.generateJava {
    schemaPaths.add("${projectDir}/src/main/resources/graphql-client")
    packageName = "com.trip_jr.tripJr.codegen"
    generateClient = true
}


tasks.register("jooqGenerate") {
    doLast {
        println("Generating jOOQ code...")
        val jdbcUrl = "jdbc:postgresql://localhost:5432/tripjr"
        val username = "postgres"
        val password = "postrespass"
        val packageName = "com.trip_jr.tripJr.jooq"
        val outputDirectory = "src/main/kotlin/com/trip_jr/tripJr/jooq"


        val jooqConfiguration = Configuration()
            .withJdbc(
                Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl(jdbcUrl)
                    .withUser(username)
                    .withPassword(password)
            )
            .withGenerator(
                Generator()
                    .withName("org.jooq.codegen.KotlinGenerator")
                    .withDatabase(
                        Database()
                            .withIncludePrimaryKeys(true)
                            .withName("org.jooq.meta.postgres.PostgresDatabase")
                            .withInputSchema("public")
                            .withIncludes(".*")
                            .withExcludes("databasechangelog|databasechangeloglock")

                    )
                    .withTarget(
                        Target()
                            .withPackageName(packageName)
                            .withDirectory(outputDirectory)
                    )

            )


        GenerationTool.generate(jooqConfiguration)
        println("jOOQ generation completed.")
    }
}