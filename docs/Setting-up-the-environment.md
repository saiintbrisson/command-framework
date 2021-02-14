# Using command-framework as a dependency

[![](https://jitpack.io/v/SaiintBrisson/command-framework.svg)](https://jitpack.io/#SaiintBrisson/command-framework)

This project is hosted by [JitPack](https://jitpack.io/#SaiintBrisson/command-framework), build artifacts can be found on their site.

## Adding the repository

### Gradle

```groovy
repositories {
    maven {
        url 'https://jitpack.io'
    }
}
```

I highly recommend using [John Engelman's Shadow gradle plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## Bukkit

### Gradle

```groovy
dependencies {
    implementation 'com.github.SaiintBrisson.command-framework:bukkit:1.0.0'
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.github.SaiintBrisson.command-framework</groupId>
        <artifactId>bukkit</artifactId>
        <version>1.0.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

## BungeeCord

### Gradle

```groovy
dependencies {
    implementation 'com.github.SaiintBrisson.command-framework:bungee:1.0.0'
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.github.SaiintBrisson.command-framework</groupId>
        <artifactId>bungee</artifactId>
        <version>1.0.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

## Shared

### Gradle

```groovy
dependencies {
    implementation 'com.github.SaiintBrisson.command-framework:shared:1.0.0'
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.github.SaiintBrisson.command-framework</groupId>
        <artifactId>shared</artifactId>
        <version>1.0.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```