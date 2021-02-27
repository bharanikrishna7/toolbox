package net.chekuri.toolbox

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.flatspec.AnyFlatSpec

class BenchmarkUtilitiesSpec extends AnyFlatSpec with LazyLogging {
  private def AddNumbers(value1: BigInt, value2: BigInt): BigInt = {
    value1.+(value2)
  }

  private def PoorNumberSquare(value: BigInt): BigInt = {
    var result: BigInt = BigInt.apply(0)
    var index = BigInt.apply(0)
    while (index.toInt < value.toInt) {
      index = index.+(1)
      result = AddNumbers(result, value)
    }
    result
  }

  "run" should "correctly run a function block and return correct results with execution time in nanoseconds" in {
    val random = new java.util.Random
    val random_integer = BigInt.apply(random.nextInt(10000))
    val expected: BigInt = random_integer.*(random_integer)
    logger.info(s"Computing square of number : $random_integer")
    val actual = BenchmarkUtilities.run[BigInt](PoorNumberSquare(random_integer))
    logger.info(s"Time taken to compute square: ${actual.execution_time_in_nanoseconds} nanos.")
    logger.info(s"Computed Square of $random_integer : ${actual.result}")
    assert(actual.execution_time_in_nanoseconds > 0)
    assert(actual.result == expected)
  }
}
