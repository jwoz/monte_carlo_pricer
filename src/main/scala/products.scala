// Products
abstract class Product
{
  var observationDateIndices = Seq[Int]()
  def payoff(underlying: Path): Double
  def closedFormPrice(): Double = ???
  def maturityDate(): Double
  def observationDates(): Seq[Double] = Seq(maturityDate())

  // def setObservationDateIndices(times: Seq[Double]) = { // don't need this function. It would make the sim quicker though.
  //   observationDateIndices = observationDates.collect {
  //     case d => times.indexWhere( t => scala.math.abs(d-t)<1e-10 )
  //   }
  // }
}

// Portfolio
class Portfolio(products: Seq[Product]) extends Product
{
  // TODO: each underlying products needs to use the right time points.
  def payoff(underlying: Path): Double = products.aggregate(0.0)( _ + _.payoff(underlying), _ + _)
  def maturityDate(): Double = observationDates.max
  override def observationDates(): Seq[Double] = products.aggregate(Seq[Double]())(_ ++ _.observationDates(), _ ++ _).sorted.distinct
}

// The underlying
class Price(date: Double) extends Product
{
  def payoff(stockPath: Path): Double = stockPath.get(maturityDate) // (observationDateIndices.head)
  def partialDerivatives(stockPrices: Path) = ???
  def maturityDate():Double = date
}

// Equity Derivatives
abstract class Option extends Product

abstract class EuropeanOption(exerciseDate: Double) extends Option
{
  def maturityDate():Double = exerciseDate
}

class Call(strike: Double, exerciseDate: Double) extends EuropeanOption(exerciseDate)
{
  def payoff(stockPrices: Path): Double = scala.math.max(stockPrices.get(maturityDate)-strike,0)
  def partialDerivatives(stockPrices: Path) = ???
  override def toString = "Call: " + strike + " " + exerciseDate
}
class Put(strike: Double, exerciseDate: Double) extends EuropeanOption(exerciseDate)
{
 def payoff(stockPrices: Path): Double = scala.math.max(strike-stockPrices.get(maturityDate),0)
}
class AsianCall(strike: Double, observationDates: Seq[Double]) extends EuropeanOption(observationDates.sorted.last)
{
  override def observationDates() : Seq[Double]= observationDates.sorted.distinct
  def payoff(stockPrices: Path): Double = {
    val averagingPrices = stockPrices.get(observationDates)
    scala.math.max(averagingPrices.sum/averagingPrices.size-strike,0)
  }
}

// Fixed income
abstract class Leg extends Product
class FixedLeg extends Leg
{
  def payoff(underlying: Path): Double = ???
  def maturityDate = ???
}
class FloatingLeg extends Leg
{
  def payoff(underlying: Path): Double = ???
  def maturityDate = ???
}

class Swap( firstLeg: Leg, secondLeg: Leg ) extends Product
{
  def payoff(underlying: Path): Double =
    firstLeg.payoff(underlying) - secondLeg.payoff(underlying)
  def maturityDate = ???
}
