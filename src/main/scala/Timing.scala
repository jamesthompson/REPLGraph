package replgraph

package object timing {

	implicit class Timer[T](f: => T) {
		def times = {
	    val before = System.currentTimeMillis
	    val result = f
	    val end = System.currentTimeMillis
	    println(s"Operation elapsed time = ${(end - before) / 1e6} s >>> Result = $result")
	    result
	  }
	  def timems = {
	    val before = System.currentTimeMillis
	    val result = f
	    val end = System.currentTimeMillis
	    println(s"Operation elapsed time = ${end - before} ms >>> Result = $result")
	    result
	  }
	  def timens = {
	    val before = System.nanoTime
	    val result = f
	    val end = System.nanoTime
	    println(s"Operation elapsed time = ${(end - before) / 1e6} ms >>> Result = $result")
	    result
	  }
	}

}