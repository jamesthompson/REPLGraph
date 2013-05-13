package replgraph

package object maths {

  import scala.collection.generic.CanBuildFrom
  import scala.collection.TraversableLike

  @inline def sqr(in: Double) : Double = in * in

  implicit class Operations[N: Numeric, M[+_]](xs: M[N]) 
    (implicit bf: CanBuildFrom[M[N], Double, M[Double]], 
              ev: M[N] => TraversableLike[N, M[N]], 
              eb: M[Double] => TraversableLike[Double, M[Double]]) {
    def normalize = {
      val n = implicitly[Numeric[N]]
      xs.isEmpty match {
        case true => None
        case false => {
          val mind = n.toDouble(xs.min)
          val maxd = n.toDouble(xs.max)
          Some(xs.map(v => (n.toDouble(v) - mind) * (1 / (maxd - mind))))
        }
      }
    }
    def geometricMean : Option[Double] = {
      val n = implicitly[Numeric[N]]
      xs.isEmpty match {
        case true => None
        case false => {
          val prod = n.toDouble(xs.product)
          val len = xs.toVector.length
          val gm = Math.pow(prod, 1.0 / len)
          if(gm.isNaN) None else Some(gm)
        }
      }
    }
    def arithmeticMean : Option[Double] = {
      val n = implicitly[Numeric[N]]
      xs.isEmpty match {
        case true => None
        case false => {
          val sum = n.toDouble(xs.sum)
          val len = xs.toVector.length
          val am = sum / len
          if(am.isNaN) None else Some(am)
        }
      }
    }
    def stdev : Option[Double] = {
      val n = implicitly[Numeric[N]]
      xs.isEmpty match {
        case true => None
        case false => {
          val sum = n.toDouble(xs.sum)
          val len = xs.toVector.length
          val avg = sum / len
          val sd = Math.sqrt(xs.map(x => sqr(n.toDouble(x) - avg)).sum / len)
          if(sd.isNaN) None else Some(sd)
        }
      }
    }
  }

}
