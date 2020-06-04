
abstract class Volatility
{
  def values( t: Seq[Double] ): Seq[Double]
  def derivatives( t: Seq[Double] ): Seq[Seq[Double]]
  def volatility( t: Double ): Double
}

class ConstantVolatility( impliedVolatility: Double ) extends Volatility
{
  def values( t: Seq[Double] ): Seq[Double] = Seq.fill[Double](t.length)(impliedVolatility)
  def derivatives( t: Seq[Double] ): Seq[Seq[Double]] = ???
  def volatility( t: Double ): Double = impliedVolatility
}

class InterpolatedTermVolatility( times: Seq[Double], volatilities: Seq[Double]) extends Volatility
{
  def values( t: Seq[Double] ): Seq[Double] = t.collect{ case t => volatility(t) }
  def derivatives( t: Seq[Double] ): Seq[Seq[Double]] = ???
  def volatility( t: Double ): Double =
  {
    // interpolate volatility here
    volatilities.head
  }
}
