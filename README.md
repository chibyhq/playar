# Chiby Playar

[![](https://jitpack.io/v/chibyhq/playar.svg)](https://jitpack.io/#chibyhq/playar) 
[![GitHub Actions Status](https://github.com/chibyhq/playar/workflows/Java%20CI/badge.svg)](https://github.com/chibyhq/playar/actions)
[![Java 17](https://img.shields.io/badge/Java-17-purple "Java 17")](https://java.com)

Chiby Playar is a container-controller daemon that provides a playlist model for container applications.
It allows to run a sequence of applications, feed them with parameters, grant them security privileges selectively etc...

# How to use

Playar is automatically deployed via Jitpack.
To include Playar in your projects, add the following to your project POM :

    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

	
Then refer to any of the playar modules using Jitpack GAV :

    <dependency>
        <groupId>com.github.chibyhq.playar</groupId>
        <artifactId>playar-client</artifactId>
        <version>${playar.version}</version>
    </dependency>

# Hacking Playar

## Compilation

You can compile using maven:

```bash
mvn install
```

### Native compilation

To generate a GraalVM native image for the `playar-store` module (requires GraalVM with `native-image` installed):

```bash
./mvnw -Pnative native:compile -pl modules/store -am
```

### Version management

To update the project version across all modules consistently:

```bash
./mvnw versions:set -DnewVersion=0.6.0-SNAPSHOT
# If everything looks good, commit the changes:
./mvnw versions:commit
# Or rollback if there was an error:
./mvnw versions:revert
```

## How to release

This project uses the maven gitflow plugin.

Simply execute :

    ./mvnw gitflow:release-start

Perform all required changes to refine the release, then execute :

    ./mvnw gitflow:release-finish
    
    
This will tag the code, perform a jitpack deploy.


