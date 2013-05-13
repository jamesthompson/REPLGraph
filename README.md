# REPLGraph

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

## Usage

You can either explicitly provide x axis data, or simply let the code generate an xaxis for you, with points separated by 1.0 for each unit. Explicit provision of x axis is done by giving the graph object a tuple with data in any TraversableLike collection, e.g. List, Seq, Vector. Data must be any basic numeric type: byte, int, short, long, float, double, bigint or bigdecimal. 

For example:

GraphType((xaxis, yaxis)) <- Explicit provision of x axis

GraphType(yaxis) <- automatic generation of x axis data (1.0 spacing between points)

n.b. if the two input axes are different lengths, the code will automatically create the x axis (as if no x axis was provided.)

case classes for use are:

LineGraph()

AreaGraph()

ScatterGraph()

PieGraph()

BarGraph()

& 

LinearFit()

## Examples - Try these out

```scala
LineGraph((xaxisdata, yaxisdata)) // where xaxisdata and yaxisdata are two lists, vectors or seq type objects. Can be different types).
AreaGraph(yaxisdata) // where the x axis values will be automatically created.
```

You can also graph as many series as you like on one graph:

```scala
ScatterGraph(yaxisdata1, yaxisdata2, yaxisdata3) // here three series will be plotted with automatically generated x axis values.

ScatterGraph((xaxisdata1, yaxisdata1), (xaxisdata2, yaxisdata2)) // here two series with x axis values provided will be plotted.
```

You can also graph categorical data such as might be needed in bar or pie charts:

```scala
PieGraph((List("these", "are", "different", "categories"), yaxisdata)) // Explicitly providing x axis categories
BarGraph(yaxisdata) // automatic naming of categories (e.g. Cat1, Cat2...Catn etc.)
```

The same applies for these graph types, as many series can be plotted as you desire in one graph object. n.b. For Pie charts the series are plotted as different pie charts.

## Linear Regression

Fits a series and returns the fit parameters on a plot.

```scala
LinearFit((xaxisdata, yaxisdata)) // x axis values provided
LinearFit(yaxisdata) // automatic x axis provision
LinearFit(yaxisdata1, (xaxisdata2, yaxisdata2), yaxisdata3) // Plot separate fits for (auto gen x, provided x, auto gen x) data series (=3 series)
```

This is just a toy so far! Please let me know if you find this useful. I use the repl a lot to test stuff out and do basic maths stuff. Graphing without having to copy is actually quite useful to me. In theory this could be quite easily expanded to save graphs, print as pdf, and have editable graphical features.


Thanks for checking it out!





