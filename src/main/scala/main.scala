
object ProductPricer extends App {

  println ("Hello world")
  val vol = new ConstantVolatility( .2 )             // market data
  val fwd = new FlatForward(20)                      // forward function
  val procSrc = new LogNormalProcessSource(fwd, vol) // model

  val stock = new Price(1.0)                         // price of the stock in 1yr
  val stockPriceSimulation = new MonteCarloSimulation(0, procSrc, stock, 100000)
  println("stockPrice in 1 year: " + stockPriceSimulation.value)

  val call = new Call(15, 1.0)                      // option at K=15, expiry 1yr
  val callPrice = new MonteCarloSimulation( 0, procSrc, new Call(15, 1.0),100000 ).value
  val putPrice =  new MonteCarloSimulation( 0, procSrc, new  Put(15, 1.0),100000 ).value
  println("stock value, call, put value and difference (should equal forward S(1yr) - K)")
  println( stockPriceSimulation.value, callPrice, putPrice, callPrice-putPrice )

  val asianCallPrice = new MonteCarloSimulation(
    0,
    procSrc,
    new AsianCall(20,Seq(0.5, 0.8, 0.9, 1)),
    100000
  ).value
  println("asian option value: " +  asianCallPrice )

  val anotherCall = new Call(400,20.0)
  val put = new Put(60,1.0)
  val asian = new AsianCall(20,Seq(0.5, 0.8, 1.0, 2.3))
  val anotherStock = new Price(3.4)

  val pf = new Portfolio(Seq(anotherCall,put,asian))
  val pf2 = new Portfolio(Seq(pf,anotherStock))

  val pfPrice = new MonteCarloSimulation(0, procSrc, pf, 10000).value
  val pf2Price = new MonteCarloSimulation(0, procSrc, pf2, 10000).value
  println("portfolio values: " + pfPrice + " " + pf2Price)
}
