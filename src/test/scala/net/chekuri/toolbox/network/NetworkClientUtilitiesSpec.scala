package net.chekuri.toolbox.network

import com.typesafe.scalalogging.LazyLogging
import net.chekuri.toolbox.BenchmarkUtilities
import org.scalatest.flatspec.AnyFlatSpec

class NetworkClientUtilitiesSpec extends AnyFlatSpec with LazyLogging {
  "getRequest" should "correctly make request and get valid response when it's guaranteed that url will return valid response (no params)" in {
    val url: String = "http://example.com"
    val response = BenchmarkUtilities.run(NetworkClientUtilities.getRequest(url))
    val result = response.result
    logger.info("Http GET Request Result:")
    logger.info(result.toJson)
    logger.info(s"Time taken to perform http get request: ${response.execution_time_in_nanoseconds} nanos.")
    logger.debug("Validate response...")
    assert(result.isValidResponse)
  }

  "getRequest" should "correctly make request and get valid response when it's guaranteed that url will return valid response (with params)" in {
    val url: String = "http://example.com/?key=value"
    val response = BenchmarkUtilities.run(
      NetworkClientUtilities.getRequest(uri = url, params = Map[String, String]("key" -> "value")))
    val result = response.result
    logger.info("Http GET Request Result:")
    logger.info(result.toJson)
    logger.info(s"Time taken to perform http get request: ${response.execution_time_in_nanoseconds} nanos.")
    logger.debug("Validate response...")
    assert(result.isValidResponse)
  }

  "getRequest" should "correctly make request and get invalid response when connection timed out" in {
    val url: String = "https://postman-echo.com/basic-auth"
    val response = BenchmarkUtilities.run(NetworkClientUtilities.getRequest(url))
    val result = response.result
    logger.info("Http GET Request Result:")
    logger.info(result.toString)
    logger.info(s"Time taken to perform http get request: ${response.execution_time_in_nanoseconds} nanos.")
    logger.debug("Validate response...")
    assert(!result.isValidResponse)
  }

  "getRequest" should "correctly make request and get valid response when it's guaranteed that url will return valid response (no params) with basic auth" in {
    val url: String = "https://postman-echo.com/basic-auth"
    val username: String = "postman"
    val password: String = "password"
    val response = BenchmarkUtilities.run(NetworkClientUtilities
      .getRequest(uri = url, params = Map[String, String]("key" -> "value"), username = username, password = password))
    val result = response.result
    logger.info("Http GET Request Result:")
    logger.info(result.toJson)
    logger.info(s"Time taken to perform http get request: ${response.execution_time_in_nanoseconds} nanos.")
    logger.debug("Validate response...")
    assert(result.isValidResponse)
  }

  "postRequest" should "correctly throw exception when authentication fails" in {
    val body =
      s"""|{
          |	"name": "blob",
          |	"auto_init": true,
          |	"private": true,
          |	"gitignore_template": "nanoc"
          |}|""".stripMargin
    val url: String = "https://api.github.com/user/repos"
    val response = BenchmarkUtilities.run(
      NetworkClientUtilities
        .postRequest(uri = url, headers = Map[String, String]("content-type" -> "raw"), body = body, username = "fake_user", password = "fake_passwd"))
    val result = response.result
    logger.info("Http GET Request Result:")
    logger.info(result.toJson)
    logger.info(s"Time taken to perform http get request: ${response.execution_time_in_nanoseconds} nanos.")
    logger.debug("Validate response...")
    assert(!result.isValidResponse)
  }
}
