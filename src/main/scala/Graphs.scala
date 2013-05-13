package replgraph

package object graphs {

  import javafx.application.{Platform, Application}
  import javafx.collections.{FXCollections, ObservableList}
  import javafx.embed.swing.JFXPanel
  import javafx.scene.chart._
  import javafx.scene.{Group, Node, Scene}
  import javafx.scene.layout.VBox
  import javafx.scene.paint.Color
  import javafx.scene.text.{Font, Text}
  import javafx.stage.Stage
  import javax.swing.{JFrame, JTextField, SwingUtilities}
  import scala.collection.generic.CanBuildFrom
  import scala.collection.TraversableLike
  import spire.math._

  type JNumber = java.lang.Number
  type TLN = TraversableLike[Number, Traversable[Number]]
  type TLS = TraversableLike[String, Traversable[String]]

  val pm = '\u00B1'
  case class LinearFitResult(val series: SN, 
                             val m: Number, 
                             val c: Number, 
                             val Rsquared: Number, 
                             val mError: Number, 
                             val cError: Number) {
    override def toString : String = s"y = (${"%.3f".format(m)} $pm ${"%.3f".format(mError)})x" + 
      s"(${"%.3f".format(c)} $pm ${"%.3f".format(cError)}), with R^2 = ${"%.3f".format(Rsquared)}"
  }

  case class SN(xs: TLN, ys: TLN) {
    lazy val points = xs.toVector.zip(ys.toVector)
  }
  case class SS(xs: TLS, ys: TLN) {
    lazy val points = xs.toVector.zip(ys.toVector)
  }

  sealed trait NGraph {
    def s : Seq[SN]
  }

  sealed trait SGraph {
    def s: Seq[SS]
  }

  case class ScatterGraph(s: SN*) extends NGraph { graphNumerical(this) }
  case class LineGraph(s: SN*) extends NGraph { graphNumerical(this) }
  case class AreaGraph(s: SN*) extends NGraph { graphNumerical(this) }
  case class BarGraph(s: SS*) extends SGraph { graphCategory(this) }
  case class PieGraph(s: SS*) extends SGraph { graphCategory(this) }
  case class LinearFit(s: SN*) extends NGraph { fitPlot(this) }

  implicit val num = compat.numeric[Number]
  def spirize[N: Numeric, M[+_]](xs: M[N])
    (implicit bf: CanBuildFrom[M[N], Number, M[Number]],
              ev: M[N] => TraversableLike[N, M[N]], 
              ef: M[Number] => TLN) : M[Number] = {
    val n = implicitly[Numeric[N]]
    xs.map(x => Number(n.toDouble(x)))
  }

  implicit def toNSeriesYS[N: Numeric, M[+_]](ys: M[N])
    (implicit bf: CanBuildFrom[M[N], Number, M[Number]],
              ev: M[N] => TraversableLike[N, M[N]], 
              ef: M[Number] => TLN) : SN = {
    val iny = spirize(ys)
    SN((0 until iny.size).toVector.map(Number(_)), iny)
  }
  implicit def toSSeriesYS[N: Numeric, M[+_]](ys: M[N])
    (implicit bf: CanBuildFrom[M[N], Number, M[Number]],
              ev: M[N] => TraversableLike[N, M[N]], 
              ef: M[Number] => TLN) : SS = {
    val iny = spirize(ys)
    SS((0 until iny.size).map("Cat " + _.toString).toVector, iny)
  }
  implicit def toNSeries[NA: Numeric, NB: Numeric, MA[+_], MB[+_]](in: (MA[NA], MB[NB]))
    (implicit cbfa: CanBuildFrom[MA[NA], Number, MA[Number]],
              eva: MA[NA] => TraversableLike[NA, MA[NA]],
              cbfb: CanBuildFrom[MB[NB], Number, MB[Number]],
              evb: MB[NB] => TraversableLike[NB, MB[NB]],
              efa: MA[Number] => TLN,
              efb: MB[Number] => TLN) : SN = {
    val inx = spirize(in._1)
    val iny = spirize(in._2)
    if(inx.size != iny.size) SN((0 until iny.size).toVector.map(Number(_)), iny) else SN(inx, iny)
  }
  implicit def toSSeries[N: Numeric, M[+_]](in: (TLS,  M[N]))
    (implicit bf: CanBuildFrom[M[N], Number, M[Number]],
              ev: M[N] => TraversableLike[N, M[N]], 
              ef: M[Number] => TLN) : SS = {
    val iny = spirize(in._2)
    if(in._1.size != iny.size) SS((0 until iny.size).map("Cat " + _.toString).toVector, iny) else SS(in._1, iny)
  } 

  def graphNumerical(l: NGraph) = {
    val frame = new JFrame()
    val panel = new JFXPanel()
    frame.add(panel)
    frame.setSize(500, 430)
    frame.setVisible(true)
    Platform.runLater(new Runnable() {
      def run() {
        val root = new Group
        val scene = new Scene(root, Color.WHITE)
        val xaxis = new NumberAxis()
        val yaxis = new NumberAxis()
        val graph = l match {
          case sg: ScatterGraph => {
            frame.setTitle(new JTextField("Scatter Chart").getText)
            scene.getStylesheets().add("ScatterChart.css")
            new ScatterChart[JNumber, JNumber](xaxis, yaxis)
          }
          case ag: AreaGraph => {
            frame.setTitle(new JTextField("Area Chart").getText)
            scene.getStylesheets().add("AreaChart.css")
            new AreaChart[JNumber, JNumber](xaxis, yaxis)
          }
          case lg: LineGraph => {
            frame.setTitle(new JTextField("Line Chart").getText)
            scene.getStylesheets().add("LineChart.css")
            new LineChart[JNumber, JNumber](xaxis, yaxis)
          }
          case _ => new LineChart[JNumber, JNumber](xaxis, yaxis)
        }
        for(g <- l.s) {
          val series : XYChart.Series[JNumber, JNumber] = new XYChart.Series()
          g.points.map(p => series.getData.add(new XYChart.Data(p._1.toDouble, p._2.toDouble)))
          series.setName(l.s.indexOf(g).toString)
          graph.getData.add(series)
        }
        root.getChildren().add(graph)
        panel.setScene(scene)
      }
    })
  }

  def graphCategory(l: SGraph) = {
    val frame = new JFrame()
    val panel = new JFXPanel()
    frame.add(panel)
    frame.setVisible(true)
    Platform.runLater(new Runnable() {
      def run() {
        val root = new Group
        val scene = new Scene(root, Color.WHITE)
        l match {
          case bg: BarGraph => {
            frame.setSize(500, 430)
            val bc : BarChart[String, JNumber] = new BarChart(new CategoryAxis(), new NumberAxis())
            for(g <- l.s) {
              val ser : ObservableList[XYChart.Data[String, JNumber]] = FXCollections.observableArrayList()
              g.points.map(p => ser.add(new XYChart.Data[String, JNumber](p._1, p._2.toDouble)))
              val series = new XYChart.Series[String, JNumber](ser)
              bc.getData.add(series)
            }
            root.getChildren().add(bc)
            frame.setTitle(new JTextField("Bar Chart").getText)
          }
          case pg: PieGraph => {
            frame.setSize(500, 430 * l.s.size)
            val vbox = new VBox()
            root.getChildren().add(vbox)
            for(g <- l.s) {
              val pcd : ObservableList[PieChart.Data] = FXCollections.observableArrayList()
              g.points.map(p => pcd.add(new PieChart.Data(p._1, p._2.toDouble)))
              frame.setTitle(new JTextField("Pie Chart").getText)
              vbox.getChildren.add(new PieChart(pcd))
            }
          }
        }
        panel.setScene(scene)
      }
    })
  }

  def fit(g: SN) : Option[LinearFitResult] = {
    try {
      def sqr(i: Number) = i * i
      val length = g.points.size
      val xbar = g.xs.sum / length
      val ybar = g.ys.sum / length
      val xxbar = g.xs.map(d => sqr(d - xbar)).sum
      val yybar = g.ys.map(d => sqr(d - ybar)).sum
      val xybar = g.points.map(d => (d._1 - xbar) * (d._2 - ybar)).sum
      val m = xybar / xxbar
      val c = ybar - (m * xbar)
      val out = for(d <- g.points) yield {
        val fit = m * d._1 + c
        (sqr(fit - d._2), sqr(fit - ybar))
      }
      val rss = out.map(_._1).sum
      val ssr = out.map(_._2).sum
      val rsquared = ssr / yybar
      val svar = rss / (length - 2).toDouble
      val stErrGradient = svar / xxbar
      val stErrIntercept = (svar / length) + (sqr(xbar) * stErrGradient)
      Some(LinearFitResult(g, m, c, rsquared, Number(math.sqrt(stErrGradient.toDouble)), Number(math.sqrt(stErrIntercept.toDouble))))
    } catch { case e: Exception => None }
  }

  def fitPlot(l: NGraph) {
    l.s.flatMap(g => fit(g)).map(f => {
      val frame = new JFrame()
      val panel = new JFXPanel()
      frame.add(panel)
      frame.setSize(500, 430)
      frame.setVisible(true)
      frame.setTitle(new JTextField(s"Fitting Chart").getText)  
      Platform.runLater(new Runnable() {
        def run() {
          val root = new Group
          val scene = new Scene(root, Color.WHITE)
          val series : XYChart.Series[JNumber, JNumber] = new XYChart.Series()
          f.series.points.map(p => series.getData.add(new XYChart.Data(p._1.toDouble, p._2.toDouble)))
          val xaxis = new NumberAxis()
          val yaxis = new NumberAxis()
          val graph = new LineChart[JNumber, JNumber](xaxis, yaxis)
          val fitSeries : XYChart.Series[JNumber, JNumber] = new XYChart.Series()
          f.series.points.map(p => fitSeries.getData.add(new XYChart.Data(p._1.toDouble, ((f.m * p._1) + f.c).toDouble)))
          graph.getData.add(series)
          graph.getData.add(fitSeries)
          series.setName("Data")
          fitSeries.setName(f.toString)
          root.getChildren().add(graph)
          scene.getStylesheets().add("FitChart.css")
          panel.setScene(scene)
        }
      })
    })
  }
  
} 