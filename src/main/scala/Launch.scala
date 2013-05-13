package replgraph

import graphs._
import scala.util.Random

object Launch extends App {

	import scala.util.Random
	val rng = new Random(System.nanoTime)
	val listx = (1 to 30).toList.map(_.toDouble)
	listx.map(x => x + (1 * rng.nextGaussian))
	ScatterGraph(listx.map(x => x + (5 * rng.nextGaussian)), listx.map(x => x + (2 * rng.nextGaussian)), listx.map(x => x + (5 * rng.nextGaussian)))
	BarGraph(List(191,2,31,45,222,281,235,251,34), List(112,12,321,4,242,251,23,25,64))
	PieGraph((List("Type1", "Cat2", "Num3", "Another", "Final"), Vector(10,24,53,125,35)))
	AreaGraph(List(112,12,321,4,242,251,23,25,64), listx.map(x => x + (5 * rng.nextGaussian)) )
	LineGraph(listx.map(x => x + (5 * rng.nextGaussian)),listx.map(x => x + (2 * rng.nextGaussian)) )
	LinearFit(listx.map(x => x + (5 * rng.nextGaussian)))

}