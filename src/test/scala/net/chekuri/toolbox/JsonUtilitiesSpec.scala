package net.chekuri.toolbox

import com.typesafe.scalalogging.LazyLogging
import net.chekuri.toolbox.JsonUtilities.JsonTraits
import net.chekuri.toolbox.io.FileUtilities
import org.scalatest.flatspec.AnyFlatSpec

import java.sql.Timestamp

class JsonUtilitiesSpec extends AnyFlatSpec with LazyLogging {
  val path: String = "src/test/resources/JsonSpecTestFile.json"
  case class JsonSpecTestFile(name: String) extends JsonTraits
  case class JsonSpecTimestampTestClass(value: Timestamp) extends JsonTraits


  "deserialize" should "properly parse a string to case class when it's a valid JSON and appropriate case class is provided." in {
    val file: String = FileUtilities.read(path).toString
    val actual: JsonSpecTestFile = JsonUtilities.deserialize[JsonSpecTestFile](file)
    val expected: JsonSpecTestFile = JsonSpecTestFile("test json spec")
    logger.debug(s"Actual: ${actual.toJson}")
    logger.debug(s"Expected: ${expected.toJson}")
    assert(actual.toJson.equals(expected.toJson))
  }

  "ToJson" should "properly convert an object to json string" in {
    val input: JsonSpecTestFile = JsonSpecTestFile("test json spec.")
    val actual: String = input.toJson
    val expected: String = """{
                             |  "name":"test json spec."
                             |}""".stripMargin
    logger.debug(s"Actual: $actual")
    logger.debug(s"Expected: $expected")
    assert(actual.equals(expected))
  }

  "ToJson" should "convert java.sql.timestamp to Local Time yyyy-MM-dd HH:mm:ss format" in {
    val expected_time_value: String = "2020-11-12 01:02:03"
    val expected_timestamp_value = Timestamp.valueOf(expected_time_value)
    //val expected_timestamp_value = Timestamp.valueOf(Time.getCurrentTimestamp("yyyy-mm-dd hh:mm:ss"))
    val payload = JsonSpecTimestampTestClass(expected_timestamp_value)
    val actual = payload.toJson
    logger.info(s"Expected Timestamp Value: ${expected_timestamp_value.toString}")
    logger.info("Actual Value:")
    logger.info(actual)
    val expected = s"""{
                      |  "value":"2020-11-12 01:02:03"
                      |}""".stripMargin
    assert(actual == expected)
  }
}
