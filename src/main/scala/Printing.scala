package replgraph


package object printing {

	import scala.collection.generic.CanBuildFrom
  import scala.collection.TraversableLike

  implicit class TraversablesPrinting[M[+_]](xs: M[_]) 
  	(implicit bf: CanBuildFrom[M[_], _, M[_]], ev: M[_] => TraversableLike[_, M[_]]) {
    def printout = {
      xs.isEmpty match {
        case true => ()
        case false => {
          println(xs.mkString("\n"))
        }
      }
    }
    def printtabs = {
      xs.isEmpty match {
        case true => ()
        case false => {
          println(xs.mkString("\t"))
        }
      }
    }
    def printcommas = {
      xs.isEmpty match {
        case true => ()
        case false => {
          println(xs.mkString(", "))
        }
      }
    }
  }

}