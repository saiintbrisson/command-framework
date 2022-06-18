# Adding as a dependency

[![](https://jitpack.io/v/SaiintBrisson/command-framework.svg)](https://jitpack.io/#SaiintBrisson/command-framework)

This project's artifacts are hosted on [JitPack](https://jitpack.io/#SaiintBrisson/command-framework).

### Using Gradle

```groovy
repositories {
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    // Add the `shared` module if you want to create custom implementations.
    implementation 'com.github.SaiintBrisson.command-framework:shared:2.0.0'
    
    // Choose between the `bukkit` or `bungee` modules depending on where you
    // plan to use the framework.
    implementation 'com.github.SaiintBrisson.command-framework:bukkit:2.0.0'
    implementation 'com.github.SaiintBrisson.command-framework:bungee:2.0.0'
}
```

I highly recommend using [John Engelman's Shadow gradle plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

### Using Maven

```xml
<project>
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.SaiintBrisson.command-framework</groupId>
            <artifactId>module</artifactId>
            <version>2.0.0</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
</project>
```