# jextract-maven-plugin

This is a Maven wrapper for the [`jextract`](https://github.com/openjdk/jextract) tool.

## Usage

```xml
<plugin>
	<groupId>io.github.coffeelibs</groupId>
	<artifactId>jextract-maven-plugin</artifactId>
	<version>0.1.0</version>
	<configuration>
		<executable>/path/to/jextract</executable>
		<headerSearchPaths>/usr/x86_64-linux-gnu/include/</headerSearchPaths>
	</configuration>
	<executions>
		<execution>
			<id>example</id>
			<goals>
				<goal>sources</goal>
			</goals>
			<configuration>
				<headerFile>point.h</headerFile>
				<libraries>
					<library>/path/to/shared/library.so</library>
				</libraries>
				<targetPackage>com.example.mypackage</targetPackage>
				<headerClassName>Point2d</headerClassName>
				<includeStructs>
					<includeStruct>Point2d</includeStruct>
				</includeStructs>
				<includeFunctions>
					<includeFunction>distance</includeFunction>
				</includeFunctions>
			</configuration>
		</execution>
	</executions>
</plugin>
```