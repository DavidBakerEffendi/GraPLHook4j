# GraPLHook4j
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/DavidBakerEffendi/GraPLHook4j.svg?branch=develop)](https://travis-ci.org/DavidBakerEffendi/GraPLHook4j)
[![codecov](https://codecov.io/gh/DavidBakerEffendi/GraPLHook4j/branch/develop/graph/badge.svg)](https://codecov.io/gh/DavidBakerEffendi/GraPLHook4j)

A Java driver for the GraPL project to provide an interface for connecting and writing to various graph databases based 
on the [code-property graph schema](https://github.com/ShiftLeftSecurity/codepropertygraph/blob/master/codepropertygraph/src/main/resources/schemas/base.json).

This CPG schema has been slightly adjusted to work with a graph database agnostic project. The models and enums can be
found under `za.ac.sun.grapl.domain`. Extensive documentation will be released with the first major release of the GraPL
project.

## Features

GraPL is currently under development. It has the following capabilities:
* Writes domain models to the graph database.
* Project an intraprocedural AST of a JVM program using JVM bytecode:
  - Package/Class/Method hierarchy
  - Variable assignments
  - Arithmetic
  - If-else bodies
* Can export an in-memory graph database to GraphML, GraphSON, and Gryo.

## Building from Source

In order to use GraPLHook4j one will need to build from source. This will be the case until the GraPL project can be 
hosted on a Maven repository or similar.

```shell script
git clone https://github.com/DavidBakerEffendi/GraPLHook4j.git
cd GraPLHook4j
mvn package
```
This will build `target/GraPLHook4j-X.X.X[-jar-with-dependencies].jar` which can then be imported into your local 
project. E.g.
```mxml
<dependency>
  <groupId>za.ac.sun.grapl</groupId>
  <artifactId>GraPLHook4j</artifactId>
  <version>X.X.X</version>
  <scope>system</scope>
  <systemPath>${project.basedir}/lib/GraPLHook4j-X.X.X.jar</systemPath>
</dependency>
``` 

## Dependencies

### Packages

The following packages are used by GraPLHook4j:

* `org.apache.logging.log4j:log4j-core:2.8.2`
* `org.apache.logging.log4j:log4j-slf4j-impl:2.8.2`
* `org.apache.tinkerpop:gremlin-core:3.4.5`

Dependencies per graph database technology being connected to:

* _TinkerGraph_ `org.apache.tinkerpop:tinkergraph-gremlin:3.4.5`

It is not recommended to use the fat jar in your project if using a build tool such as Ant, Maven, Gradle, etc. Rather
use the main artifact and add the dependencies manually (in your `pom.xml`, `build.gradle`, etc.). Note that if you are
connecting to Neo4j, for example, you would not need the TinkerGraph, TigerGraph, etc. dependencies. 

### Java Support

The following versions of Java are officially supported:
* OpenJDK 8
* OpenJDK 9
* OpenJDK 10
* OpenJDK 11

### Graph Database Support

Databases supported:
* TinkerGraph

Planned to support in the near future:
* TigerGraph
* Neo4j
* JanusGraph

## Basic Process

GraPLHook4j works through immutable domain objects, and a high level API in order to construct and analyse a
code-property graph. [Java ASM5](https://asm.ow2.io/) notation is used for method signatures and representing arrays and
types. An example of this is:
```java
import za.ac.sun.grapl.domain.enums.EvaluationStrategies;
import za.ac.sun.grapl.domain.models.vertices.BlockVertex;
import za.ac.sun.grapl.domain.models.vertices.FileVertex;
import za.ac.sun.grapl.domain.models.vertices.MethodVertex;
import za.ac.sun.grapl.hooks.TinkerGraphHook;

public class GraPLDemo {

    public static void main(String[] args) {
        int order = 0;
        int lineNumber = 1;
        TinkerGraphHook hook = new TinkerGraphHook.TinkerGraphHookBuilder("/tmp/grapl/j2grapl_demo.xml")
                                                .createNewGraph(true)
                                                .build();
        FileVertex fileVertex = new FileVertex("GraPLTest", order++);
        hook.createVertex(fileVertex);
        MethodVertex methodVertex = new MethodVertex("add", "GraPLTest.add", "II", lineNumber, order++);
        hook.createVertex(methodVertex);
        hook.joinFileVertexTo(fileVertex, methodVertex);
        hook.createAndAddToMethod(
                this.methodVertex,
                new MethodReturnVertex("VOID", "V", EvaluationStrategies.BY_VALUE, lineNumber, order++)
        );
        // ...
        // etc.
        hook.exportCurrentGraph();
    }   

}
```
This exported file can then be visualized using tools such as [Cytoscape](https://cytoscape.org/).

## Logging

All logging can be configured under `src/main/resources/log4j2.properties`. By default, all logs can be found under 
`/tmp/grapl`.