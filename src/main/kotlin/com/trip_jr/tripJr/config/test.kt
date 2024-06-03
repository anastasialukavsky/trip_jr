package com.trip_jr.tripJr.config

//class test {
//
//    import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//    import io.github.kobylynskyi.graphql.codegen.gradle.GraphQLCodegenGradleTask
//    import org.jooq.meta.jaxb.*
//    import org.jooq.codegen.GenerationTool
//    import org.jooq.meta.jaxb.Target
//
//    plugins {
//        id("org.springframework.boot") version "3.1.3"
//        id("io.spring.dependency-management") version "1.1.3"
//        id("io.github.kobylynskyi.graphql.codegen") version "5.8.0"
//        kotlin("jvm") version "1.8.22"
//        kotlin("plugin.spring") version "1.8.22"
//    }
//
//    group = "com.learnathon"
//    version = "0.0.1-SNAPSHOT"
//
//    val packageName = "com.learnathon.mini-tripadvisor"
//
//    java {
//        sourceCompatibility = JavaVersion.VERSION_17
//    }
//
//    buildscript {
//        repositories {
//            mavenCentral()
//        }
//        dependencies {
//            classpath("org.jooq:jooq-codegen:3.16.6")
//            classpath("org.postgresql:postgresql:42.3.5")
//        }
//    }
//
//    repositories {
//        mavenCentral()
//        mavenLocal()
//    }
//
//    dependencies {
//        implementation("org.springframework.boot:spring-boot-starter-graphql")
//        implementation("org.springframework.boot:spring-boot-starter-web")
//        implementation("org.springframework.boot:spring-boot-starter-jooq")
//        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//        implementation("org.jetbrains.kotlin:kotlin-reflect")
//        implementation("io.github.kobylynskyi:graphql-java-codegen:5.8.0")
//        implementation("javax.validation:validation-api:2.0.1.Final")
//        testImplementation("org.springframework.boot:spring-boot-starter-test")
//        testImplementation("org.springframework:spring-webflux")
//        testImplementation("org.springframework.graphql:spring-graphql-test")
//
//        implementation("org.jooq:jooq:3.18.7")
//        implementation("org.jooq:jooq-meta:3.18.7")
//        implementation("org.jooq:jooq-codegen:3.18.7")
//        implementation("org.jooq:jooq-meta-extensions:3.18.7")
//        implementation("org.postgresql:postgresql:42.5.4")
//    }
//
//    tasks.withType<KotlinCompile> {
//        kotlinOptions {
//            freeCompilerArgs += "-Xjsr305=strict"
//            jvmTarget = "17"
//        }
//    }
//
//    tasks.withType<Test> {
//        useJUnitPlatform()
//    }
//
//    tasks.named<GraphQLCodegenGradleTask>("graphqlCodegen") {
//        // all config options:
//        // https://github.com/kobylynskyi/graphql-java-codegen/blob/main/docs/codegen-options.md
//        graphqlSchemaPaths = listOf("$projectDir/src/main/resources/graphql/schema.graphqls")
//        outputDir = File("$buildDir/generated")
//        packageName = "learnathon.minitripadvisor.graphql"
//        customTypesMapping = mutableMapOf(Pair("EpochMillis", "java.time.LocalDateTime"))
//        customAnnotationsMapping = mutableMapOf(Pair("EpochMillis", listOf("@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.example.json.EpochMillisScalarDeserializer.class)")))
//    }
//
//
//    tasks.create("jooqCodegen") {
//        GenerationTool.generate(Configuration()
//            .withJdbc(Jdbc()
//                .withDriver("org.postgresql.Driver")
//                .withUrl("jdbc:postgresql://localhost:5432/mini_tripadvisor?user=jbradbeer"))
//            .withGenerator(Generator()
//                .withDatabase(Database()
//                    .withName("org.jooq.meta.postgres.PostgresDatabase")
//                    .withInputSchema("public"))
//                .withGenerate(Generate())
//                .withTarget(Target()
//                    .withPackageName("learnathon.minitripadvisor.jooq")
//                    .withDirectory("$buildDir/generated"))))
//    }
//
//
//    tasks.register("generate") {
//        dependsOn(
//            "graphqlCodegen",
//            "jooqCodegen"
//        )
//    }
//
//// Automatically generate GraphQL code on project build:
//    sourceSets {
//        getByName("main").java.srcDirs("$buildDir/generated")
//    }
//
//    tasks.compileKotlin.get().dependsOn(
//    "generate"
//    )
//}