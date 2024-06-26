import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Target

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
    maven { url = uri("https://mvnrepository.com/artifact/software.amazon.awssdk") }
}

extra["springAiVersion"] = "0.8.1"
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:15.1.0") {
        exclude(group = "com.graphql-java", module = "graphql-java-extended-scalars")
    }
//    implementation("com.graphql-java-kickstart:graphiql-upload-spring-boot-starter:15.1.0")
    implementation("com.graphql-java:graphql-java-extended-scalars:21.0")
    implementation("com.graphql-java-kickstart:graphql-java-tools:13.1.1") {
        exclude(group = "com.graphql-java", module = "graphql-java-extended-scalars")
    }
    implementation("com.graphql-java:graphql-java-extended-scalars:21.0") {
        version {
            strictly("[21.0]")
        }
    }
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("software.amazon.awssdk:s3:2.25.67")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    implementation("commons-fileupload:commons-fileupload:1.5")

//    implementation("org.springframework.cloud:spring-cloud-starter:4.1.3")
    implementation("net.coobird:thumbnailator:0.4.20")

    //TODO configure spring security
//    implementation ("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.security:spring-security-config")
//    implementation("org.springframework.security:spring-security-web")


//    implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
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