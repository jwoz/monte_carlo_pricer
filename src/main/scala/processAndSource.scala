 import scala.util.Random

trait RandomNumbers {
  val rdn = new Random()
  def normal() = rdn.nextGaussian()
  def normals( n: Int ) = Seq.tabulate(n)(n => normal())
}

// stochastic processes
abstract class Process
{
  def values: Seq[Double]
  def jacobian: Array[Array[Double]]
}

abstract class ProcessSource
{
  def build(t: Seq[Double]): Process
}

class LogNormalProcessSource( fwd: Forward, sigma: Volatility) extends ProcessSource
{
  def build(times: Seq[Double]): Process = new LogNormalProcess( fwd.values(times), sigma.values(times), times)
}


class SeriallyCorrelatedProcess( sigma: Seq[Double], times: Seq[Double] ) extends Process with RandomNumbers
{
  private val timedVols = (sigma zip times).collect{ case sv => sv._1*scala.math.sqrt(sv._2) }
  private val serialCorrs = timedVols.+:(0.0).sliding(2).collect{ case p => scala.math.sqrt(p.head/p.last) }.toSeq
  private val numPoints = timedVols.length

  private var normalNumbers: Seq[Double] = Seq()
  private var correlNumbers: Seq[Double] = Seq()
  def values =
  {
    var lastrdn=0.0
    def nextrdn(p: (Double, Double)): Double =
    {
      import scala.math.sqrt
       val rho = p._1
      lastrdn = lastrdn * rho + sqrt(1.0-rho*rho)*p._2
      lastrdn
    }
    normalNumbers=super.normals(timedVols.length)
    correlNumbers=(serialCorrs zip normalNumbers).collect{ case p => nextrdn(p) }.toSeq
    correlNumbers
  }

  private def populateJacobian(offDiagonal: Seq[Double], i: Int, j: Int) =
  {
    if ( i<j ) 0f
    else 1f
  }

  // derivatives wrt sigma for each random number
  def jacobian: Array[Array[Double]] =
  {
    val offDiagonal =
      (correlNumbers zip
        ((normalNumbers zip serialCorrs).collect{ case nr => nr._2/scala.math.sqrt(1-nr._2*nr._2)*nr._1 }.toSeq)
      ).collect{ case cns => cns._1 - cns._2 }.toSeq
    Array.tabulate(numPoints,numPoints)( (i,j) => populateJacobian(offDiagonal,i,j) )
  }
}

class NormalProcess( fwd: Seq[Double], sigma: Seq[Double], times: Seq[Double] ) extends Process with RandomNumbers
{
  def values = times.collect{ case t => super.normal() }
  def jacobian = ???
}

class LogNormalProcess( fwd: Seq[Double], sigma: Seq[Double], times: Seq[Double] )
    extends SeriallyCorrelatedProcess( sigma, times )
{
  import scala.math.{exp, sqrt}

  assert(fwd.length==sigma.length)
  assert(sigma.length==times.length)

  private val timedVols=(sigma zip times).collect{ case sv => sv._1*sqrt(sv._2) }
  private val dTimedVolsdSigma = times.collect{ case t => sqrt(t) }

  override def values =
  {
    val srlZs = super.values
    assert(srlZs.length==timedVols.length)
    val volPath = (timedVols zip srlZs).collect{ case tvZ => exp( -0.5 * tvZ._1 * tvZ._1 + tvZ._1 * tvZ._2 ) }
    assert(volPath.length==fwd.length)
    val riskTest = jacobian
    (fwd zip volPath).collect{ case fv => fv._1 * fv._2 }
  }
  // the Jacobian matrix wrt fwd (fwd-Delta) and sigma (vega) at each time point
  // complicated by the serial correlation
  override def jacobian = super.jacobian
}

//class HullWhiteProcess() extends Process with RandomNumbers
// Ornstein Uhlenbeck in terminal measure
