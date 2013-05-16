package replgraph

import org.specs2.mutable._

object ReplGraphSpec extends Specification {
	
	import graphs._
	import scala.collection.generic.CanBuildFrom
  import scala.collection.TraversableLike
	
		"graphs package object" should {
			
			"automagically convert any List[Int] object to a List[Number]" in	{
				val sp = spirize(List(1, 2, 3)) 
				sp must_== (List[spire.math.Number](spire.math.Number(1.0), spire.math.Number(2.0), spire.math.Number(3.0)))
			}

			"automagically convert any Vector[Double] object to a Vector[Number]" in {
				val sp = spirize(Vector(1.0, 2.0, 3.0)) 
				sp must_== (Vector[spire.math.Number](spire.math.Number(1.0), spire.math.Number(2.0), spire.math.Number(3.0)))
			}

			"automagically convert any Seq[BigInt] object to a Seq[Number]" in {
				val sp = spirize(Seq(BigInt(1), BigInt(2), BigInt(3))) 
				sp must_== (Seq[spire.math.Number](spire.math.Number(1.0), spire.math.Number(2.0), spire.math.Number(3.0)))
			}

		}

}