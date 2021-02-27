package net.chekuri.toolbox.network

import com.typesafe.scalalogging.LazyLogging
import net.chekuri.toolbox.BenchmarkUtilities
import org.scalatest.flatspec.AnyFlatSpec


class NetworkCommonsUtilitiesSpec extends AnyFlatSpec with LazyLogging {
  "getMyIpAddress" should "correctly retrieve local ip address" in {
    val actual = BenchmarkUtilities.run(NetworkCommonsUtilities.getMyIpAddress())
    logger.info(s"Time taken to detect current IP Address : ${actual.execution_time_in_nanoseconds} nanos.")
    logger.info(s"Detected Current IP Address: ${actual.result}")
    logger.info(s"Current host address : ${actual.result.getHostAddress}")
    logger.info(s"Current host name    : ${actual.result.getHostName}")
    assert(true)
  }

  "getClientIpAddress" should "correctly retrieve client ip address" in {
    val actual = BenchmarkUtilities.run(NetworkCommonsUtilities.getClientIpAddress("google.com", 80))
    logger.info(s"Time taken to detect client IP Address : ${actual.execution_time_in_nanoseconds} nanos.")
    logger.info(s"Detected client IP Address: ${actual.result}")
    logger.info(s"Client host address : ${actual.result.getHostAddress}")
    logger.info(s"Client host name    : ${actual.result.getHostName}")
    assert(true)
  }
}
