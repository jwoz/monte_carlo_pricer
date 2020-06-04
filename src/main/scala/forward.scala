// forward functions
abstract class Forward
{
  def values(t: Seq[Double]): Seq[Double]
}
class FlatForward( val forward: Double ) extends Forward
{
  def values( t: Seq[Double] ) = t.collect{ case tt => forward }
}
class ExponentialForward( val initialPrice: Double, val rate: Double ) extends Forward
{
  import scala.math.exp
  def values( t: Seq[Double] ) = t.collect{ case tt => initialPrice*exp(rate*tt) }
}

