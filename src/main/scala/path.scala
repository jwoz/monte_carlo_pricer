import scala.collection.mutable.Buffer
case class Path(val simulationTimes:Seq[Double] = Seq[Double](), var path: Buffer[Double] = Buffer[Double]())
{
  def isEmpty = path.isEmpty
  def last = path.last
  def get(time: Double): Double = path( simulationTimes.indexWhere( t => scala.math.abs(time-t)<1e-10 ) )
  def get(times: Seq[Double]): Seq[Double] = {
    val indices = times.collect {
      case d => times.indexWhere( t => scala.math.abs(d-t)<1e-10 )
    }
    indices.collect{ case i => path(i) }
  }
  def append(d: Double):Unit = path +=(d)
  def append(s: Seq[Double]):Unit = s.foreach(ss=>this.append(ss))
  def add(that: Path): Unit = that.path.view.zipWithIndex.foreach{case (value, index) => this.path(index) += value}
  override def toString = "Path " + path.length + " " + path.mkString(", ")
}

// class RiskyPath(val simulationTimes: Seq[Double] = Seq[Double](), var path: Buffer[Double] = Buffer[Double]()) extends Path
// {
//   // TODO put risk here
// }
