package net.chekuri.toolbox.network

import com.typesafe.scalalogging.LazyLogging
import net.chekuri.toolbox.JsonUtilities.JsonTraits
import okhttp3.Request

import java.io.UnsupportedEncodingException
import java.net.{InetAddress, SocketException, URLEncoder}

object NetworkCommonsUtilities extends LazyLogging {

  /** Method to retrieve machine's current IP address.
    *
    * @param host host to try establishing connection to.
    * @param port port associated to host which we are establishing connection to.
    * @throws SocketException when access network is down, or when host is inaccessible.
    * @return local ip address
    */
  @throws[SocketException]
  def getMyIpAddress(host: String = "google.com", port: Int = 80): InetAddress = {
    import java.net.InetSocketAddress
    import java.net.Socket
    val socket = new Socket
    socket.connect(new InetSocketAddress(host, port))
    socket.getLocalAddress
  }

  /** Method to retrieve client machine's IP address.
    *
    * @param host client host
    * @param port client port
    * @throws SocketException when network access is down, or when host is inacessible.
    * @return client ip address
    */
  @throws[SocketException]
  def getClientIpAddress(host: String, port: Int): InetAddress = {
    import java.net.InetSocketAddress
    import java.net.Socket
    val socket = new Socket
    socket.connect(new InetSocketAddress(host, port))
    socket.getInetAddress
  }

  /** Method to safely escape query string parameters and build
    * the string while making request. This will take care of
    * escaping spaces and other special symbols in http request url.
    *
    * @param url    http url without query string params
    * @param params query string params
    * @throws UnsupportedOperationException when supplied query string
    *                                       parameter cannot be converted to UTF-8 encoding.
    * @return UTF-8 encoded, special characters escaped http request string value.
    */
  @throws[UnsupportedEncodingException]
  def urlBuilder(url: String, params: Map[String, String]): String = {
    if (params.isEmpty) {
      url
    } else {
      val paramBuilder: StringBuffer = new StringBuffer()
      for ((k, v) <- params) {
        paramBuilder.append(URLEncoder.encode(k, "UTF-8"))
        paramBuilder.append('=')
        paramBuilder.append(URLEncoder.encode(v, "UTF-8"))
        paramBuilder.append('&')
      }
      val paramsString = paramBuilder.substring(0, paramBuilder.length() - 1)
      s"$url?$paramsString"
    }
  }

  def buildRequest(uri: String, params: Map[String, String], headers: Map[String, String]): Request.Builder = {
    logger.debug("Building request from supplied arguments...")
    logger.trace("Generating UTF-8 encoded http url.")
    val url: String = this.urlBuilder(uri, params)
    logger.debug(s"Prepared URL : $url")
    logger.trace("Initializing request builder.")
    var request: Request.Builder = new Request.Builder()
      .url(url)
    logger.trace("Adding supplied headers to request builder.")
    for ((k, v) <- headers) {
      request = request.addHeader(URLEncoder.encode(k, "UTF-8"), URLEncoder.encode(v, "UTF-8"))
    }
    logger.trace("Returning request builder object.")
    request
  }

  case class HttpResponse[R](response: R, code: Int, headers: Map[String, String], message: String, content_length: Int)
    extends JsonTraits {
    /** Method to check if the response is a valid object.
      *
      * @return true when content length >= 0, else false.
      */
    def isValidResponse: Boolean = {
      if (content_length < 0 || code / 100 == 4) false else true
    }
  }
}
