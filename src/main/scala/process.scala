// OLD, superseded by processAndSource.scala

// import scala.util.Random

// package oldProcess {

//   trait RandomNumbers {
//     val rdn = new Random()
//     def normal() = rdn.nextGaussian()
//   }

//   // stochastic processes
//   abstract class Process
//   {
//     def value(t: Double): Double
//     def values(t: Seq[Double]): Seq[Double]
//     def derivatives(t: Seq[Double]): Seq[Double]
//   }

//   abstract class NormalProcess extends Process with RandomNumbers
//   {
//     def value(t: Double) = super.normal()
//     def values(t: Seq[Double]) = t.collect{ case t => super.normal() }
//     def derivatives(t: Seq[Double]) = ???
//   }

//   class LogNormalProcess( val sigma: Double ) extends Process with RandomNumbers
//   {
//     // todo... make sigma a function of time and add ratio in serial correlation
//     // todo... build one LogNormalProcess class for each vector t
//     import scala.math.{exp, sqrt}
//     override def value( t: Double ): Double =
//     {
//       val timeVol = scala.math.sqrt(t)*sigma
//       exp( -0.5*timeVol*timeVol + timeVol*super.normal())
//     }
//     override def values( t: Seq[Double] ) =
//     {
//       var lastrdn=0.0
//       def nextrdn(p: Seq[Double]): Double =
//       {
//         assert( p.size == 2 )
//         val rho = sqrt(p.head/p.last)
//         lastrdn = lastrdn * rho + sqrt(1.0-rho*rho)*value(p.last)
//         lastrdn
//       }
//       t.+:(0.0).sliding(2).collect{ case p => nextrdn(p) }.toSeq
//     }
//     def derivatives(t: Seq[Double]) = ???
//   }

//   //class HullWhiteProcess() extends Process with RandomNumbers
//   // Ornstein Uhlenbeck in terminal measure

// }
