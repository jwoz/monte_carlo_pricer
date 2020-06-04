class MonteCarloSimulation(
  val valuationDate: Double, // TODO this is probably not used right when generating serial correlation
  val processSource: ProcessSource,
  val product: Product,
  val iterations: Int) //extends Valuation( volModel, product )
{
  val accumulator = new Iterations(iterations)
  var pricePath = new Path(product.observationDates)
  val process = processSource.build(product.observationDates)

  def value(): Double =
  {
    while ( accumulator.continue )
    {
      pricePath.path=process.values.toBuffer
      pricePath.append(product.payoff(pricePath))
      accumulator.add(pricePath)
    }
    accumulator.expectation
  }
}

// // simulate underlying prices (FX rates, stock prices...)
// class PriceSimulator( val processSource: ProcessSource )
// {
//   var times: Seq[Double]
//   var process: Process
//   def initialize(ts: Seq[Double]) =
//   {
//     times=ts // don't think I need this
//     process = processSource.build(ts)
//   }

//   def values(path: Path) =
//     if (!(times.isEmpty))
//     {
//       path.path = process.values.toBuffer
//     }
// }
