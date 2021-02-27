package net.chekuri.toolbox

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.flatspec.AnyFlatSpec

import java.sql.Timestamp

class TimeUtilitiesSpec extends AnyFlatSpec with LazyLogging {
  "TimestampToString" should "correctly convert timestamp to appropriate string value" in {
    val text_timestamp = TimeUtilities.CurrentTimestamp.toString
    val generated_timestamp = Timestamp.valueOf(text_timestamp)
    val actual = TimeUtilities.TimestampToString(generated_timestamp, "yyyy-MM-dd HH:mm:ss.SSS")
    logger.info(s"Expected: $text_timestamp")
    logger.info(s"Actual  : $actual")
    assert(actual == text_timestamp)
  }

  "CurrentDate" should "correctly return current date value" in {
    val date = TimeUtilities.CurrentDate
    val timestamp = TimeUtilities.CurrentTimestamp
    logger.info(s"Current Date      Value : ${date.toString}")
    logger.info(s"Current Timestamp Value : ${timestamp.toString}")
    assert(date.getYear == timestamp.getYear)
    assert(date.getMonth == timestamp.getMonth)
    assert(date.getDate == timestamp.getDate)
    assert(date.getTime <= timestamp.getTime)
  }
}
