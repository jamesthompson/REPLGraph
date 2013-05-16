package replgraph

import org.specs2.mutable._

object ReplGraphSpec extends Specification {
	
	import graphs._
	
		"conversions from traversables[Numeric] objects to traversables[spire.math.Number]" should {
			
			"be possible from a List[Int] object to a List[Number]" in	{
				val sp = spirize(List(1, 2, 3)) 
				sp must_== (List[spire.math.Number](spire.math.Number(1.0), spire.math.Number(2.0), spire.math.Number(3.0)))
			}

			"be possible from a Vector[Double] object to a Vector[Number]" in {
				val sp = spirize(Vector(1.0, 2.0, 3.0)) 
				sp must_== (Vector[spire.math.Number](spire.math.Number(1.0), spire.math.Number(2.0), spire.math.Number(3.0)))
			}

			"be possible from a Seq[BigInt] object to a Seq[Number]" in {
				val sp = spirize(Seq(BigInt(1), BigInt(2), BigInt(3))) 
				sp must_== (Seq[spire.math.Number](spire.math.Number(1.0), spire.math.Number(2.0), spire.math.Number(3.0)))
			}

		}

		"implicit conversions" should {

			"give back a numerical series SN when given a one dimensional input (i.e. no x axis)" in {
				val sn : SN = List(1,2,3)
				sn must beAnInstanceOf[SN]
			}

			"give back a numerical series SN when given a two dimensional input (i.e. with an x axis)" in {
				val sn : SN = (List(1,2,3), Vector(1.0, 2.0, 3.0))
				sn must beAnInstanceOf[SN]
			}

			"give back a categorical series SS when given a one dimensional input (i.e. no x axis)" in {
				val ss : SS = List(3,2,1)
				ss must beAnInstanceOf[SS]
			}

			"give back a numerical series SN when given a two dimensional input (i.e. with an x axis)" in {
				val ss : SS = (List("This","is a", "unit test"), Vector(1.0, 2.0, 3.0))
				ss must beAnInstanceOf[SS]
			}

		}

		"fitting through linear regression by implicit conversions" should {

			"be possible by providing only a traversable[Numeric]" in {
				fit(List(1,2,3,4,5,6,7,8,9,10)) must haveClass[Some[LinearFitResult]]
			}

			"be possible by providing an actual Series" in {
				fit((List(1,2,3,4,5), List(1,2,3,4,5))) must haveClass[Some[LinearFitResult]]
			}

		}

}