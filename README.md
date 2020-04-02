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

## Basic Process

GraPLHook4j works through immutable domain objects and a high level API in order to construct and analyse a 
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

## Support

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
