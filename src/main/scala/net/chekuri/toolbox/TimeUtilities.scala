package net.chekuri.toolbox

import com.typesafe.scalalogging.LazyLogging

import java.sql.{Date, Timestamp}
import java.util.{Date => JDate}

object TimeUtilities extends LazyLogging {
  val default_pattern: String = "yyyy-MM-dd HH:mm:ss"

  /** Method to generate current java date value.
    * For all purposes it's used as timestamp.
    * @return java.util.Date object associated with current timestamp.
    */
  def GenerateCurrentJavaDate = new JDate()

  /** Method to generate current timestamp value.
    * @return java.sql.Timestamp object associated with current timestamp.
    */
  def CurrentTimestamp: Timestamp = new Timestamp(GenerateCurrentJavaDate.getTime)

  /** Method to generate current date value.
    * @return java.sql.Date object associated with current Date (for timestamp please use CurrentTimestamp method).
    */
  def CurrentDate: Date = {
    val current = GenerateCurrentJavaDate
    val stripped = new JDate(current.getYear, current.getMonth, current.getDate)
    new Date(stripped.getTime)
  }

  /** Method to convert timestamp to string value.
    * @param timestamp timestamp
    * @param pattern pattern
    * @return String value associated with supplied timestamp
    */
  def TimestampToString(timestamp: Timestamp, pattern: String = default_pattern): String = {
    val java_date = new JDate(timestamp.getTime)
    JavaDateToString(date = java_date, pattern = pattern)
  }

  /** Method to convert java date to string value.
    * @param date java date
    * @param pattern pattern
    * @return String value associated with supplied java date
    */
  def JavaDateToString(date: JDate, pattern: String = default_pattern): String = {
    import java.text.SimpleDateFormat
    val formatter = new SimpleDateFormat(pattern)
    formatter.format(date)
  }
}
