package net.chekuri.toolbox

import com.typesafe.scalalogging.LazyLogging
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.writePretty

/** Singleton object containing commonly
  * used methods to perform Json serialization
  * and deserialization.
  *
  * NOTE:
  *
  * Ideally for all variables in a class
  * to be correctly initialized we expect the
  * class to have proper getters and setters like POJO.
  *
  * Scala case class | Java POJO classes can be directly
  * used without any issues.
  */
object JsonUtilities extends LazyLogging {

  /** Interface which will allow all it's
    * derived class can automatically
    * serialize object into JSON.
    *
    * NOTE:
    *
    * Ideally for all variables in a class
    * to be correctly initialized we expect the
    * class to have proper getters and setters like POJO.
    *
    * Scala case class | Java POJO classes can be directly
    * used without any issues.
    */
  trait JsonTraits {
    implicit val formats: DefaultFormats = new DefaultFormats {
      override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }
    def toJson: String = Serialization.writePretty(this)
    override def toString: String = this.toJson
  }

  // Brings in default date formats etc.
  implicit val formats: DefaultFormats.type = DefaultFormats

  /** Method to parse a json string to Case Class / Class.
    * @param json json string to parse
    * @param m Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return Case Class / Class object with Json Values.
    */
  def deserialize[T](json: String)(implicit m: Manifest[T]): T = {
    logger.trace("Parsing Json to Object.")
    parse(json).extract[T]
  }

  /** Method to parse a json string to Case Class / Class.
    * @param json json byte array to parse
    * @param m Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return Case Class / Class object with Json Values.
    */
  def deserialize[T](json: Array[Byte])(implicit m: Manifest[T]): T = {
    logger.trace("Parsing Json to Object.")
    val stream = new ByteArrayInputStream(json)
    parse(stream).extract[T]
  }

  /** Method to convert a Case Class / Class to Json String.
    * @param payload case class/class we want to convert to json string
    * @param m Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return Json String value associated with supplied Case Class / Class.
    */
  def serialize[T](payload: T)(implicit m: Manifest[T]): String = {
    logger.trace("Converting Object to Json.")
    writePretty(payload)
  }
}
