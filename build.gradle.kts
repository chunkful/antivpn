import xyz.jpenilla.gremlin.gradle.ShadowGremlin
import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
    `java-library`
    // Uncomment to use paperweight-userdev
    // alias(libs.plugins.paperweightUserdev)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.resourceFactory.paper)
    alias(libs.plugins.gremlin)
    alias(libs.plugins.shadow)
}

group = "de.mcmdev"
version = "1.0.0"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Uncomment to use paperweight-userdev
    // paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.paperApi)
    compileOnly(libs.luckperms)
    runtimeDownload(libs.httpclient)
    runtimeDownload(libs.ormlite.core)
    runtimeDownload(libs.ormlite.jdbc)
    runtimeDownload(libs.jackson.databind)
    runtimeDownload(libs.jackson.yaml)
    runtimeDownload(libs.jexl)

    testImplementation(libs.httpclient)
    testImplementation("com.h2database:h2:2.4.240")
    testImplementation(libs.luckperms)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.yaml)

    /*
    Dependencies declared with `implementation` will be shaded into the final jar.
    Dependencies declared with `compileOnly` will be available at compile time but not shaded into the final jar.
    Dependencies declared with `runtimeDownload` will be downloaded at runtime and not shaded into the final jar.
     */
}

tasks {
    jar {
        enabled = false
    }
    assemble {
        dependsOn(shadowJar)
        dependsOn(writeDependencies)
    }
    shadowJar {
        archiveClassifier.set("")
    }
    runServer {
        minecraftVersion("1.21.10")
        jvmArgs("-XX:+AllowEnhancedClassRedefinition")
    }
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    test {
        failOnNoDiscoveredTests = false
    }
}

fun reloc(fromPkg: String, toPkg: String) {
    listOf(tasks.shadowJar, tasks.writeDependencies).forEach { task ->
        task.configure {
            ShadowGremlin.relocate(this, fromPkg, toPkg)
        }
    }
}

reloc("tools.jackson", "de.mcmdev.elements.lib.tools.jackson")
reloc("com.fasterxml.jackson", "de.mcmdev.elements.lib.com.fasterxml.jackson")
reloc("org.yaml", "de.mcmdev.elements.lib.org.yaml")

configurations.compileOnly {
    extendsFrom(configurations.runtimeDownload.get())
}
configurations.testImplementation {
    extendsFrom(configurations.runtimeDownload.get())
}

paperPluginYaml {
    main = "de.mcmdev.antivpn.AntiVpnPlugin"
    loader = "xyz.jpenilla.gremlin.runtime.platformsupport.DefaultsPaperPluginLoader"
    apiVersion = "1.21"
    dependencies {
        server {
            register("LuckPerms") {
                required = true
                load = PaperPluginYaml.Load.BEFORE
            }
        }
    }
}