abstract class Accumulator
{
  var paths = new Path
  def add( newIteration: Path ) = {
    if (paths.isEmpty)
      paths = newIteration.copy()
    else
      paths.add(newIteration)
  }

  def continue: Boolean
  def expectation: Double
  def uncertainty: Double

  override def toString = "Accumulator " + paths
}

class Iterations(val numberOfIterations: Int) extends Accumulator
{
  private var i: Int = 0
  def continue = if (i<numberOfIterations)
  {
    i+=1
    true
  }
  else false

  def expectation = {
    paths.path.collect{ case p => p/numberOfIterations }.last
  }
  def uncertainty = Double.NaN

  override def toString = "Iterations::"+super.toString
}

