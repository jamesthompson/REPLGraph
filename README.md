# REPLGraph
=========

Scala implicit conversions to make GUI graphs using JavaFX and Swing.

Simple graphing for the REPL. Plot JavaFX line, area, scatter, bar and pie graphs easily. Also do linear regression and plot the results straightforwardly. 

## Intro

Download the package. Building is done with SBT.
This is only tested on 1 machine so far! Running Mac OS X and the Oracle JRE for Java 1.8. JavaFX and the other necessary dependencies are included in the jar using Assembly.

n.b. MUST have Scala 2.10 +

## Quick Start

To build use :

```
sbt assembly
```

replgraph.jar created in target/scala-2.10/ directory.

To use it, add replgraph.jar to your repl classpath thus:

```
scala -cp replgraph.jar
```

then import the package object:

```scala
import replgraph.graphs._
```



