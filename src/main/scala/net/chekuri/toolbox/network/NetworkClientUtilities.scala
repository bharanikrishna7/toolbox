package net.chekuri.toolbox.network

import com.typesafe.scalalogging.LazyLogging
import net.chekuri.toolbox.network.NetworkCommonsUtilities.HttpResponse
import okhttp3.{Credentials, OkHttpClient, Request, RequestBody, Response}

import java.io.IOException
import java.net.SocketTimeoutException
import scala.collection.JavaConverters.asScalaSetConverter

object NetworkClientUtilities extends LazyLogging {

  def getRequest(
    uri: String,
    params: Map[String, String] = Map[String, String](),
    headers: Map[String, String] = Map[String, String](),
    username: String = "",
    password: String = ""
  ): HttpResponse[String] = {
    val credentials = if (username == "" || password == "") {
      ""
    } else {
      this.generateBasicCredentials(username, password)
    }
    this.getRequestWithAuth(uri = uri, params = params, headers = headers, credentials = credentials)
  }

  /** Method to make http get request and return http response
    * @param uri url without query string params.
    *
    * NOTE:
    *
    * This method will not throw exception when:
    *
    * 1. either request was cancelled before response.
    *
    * 2. request time-out
    *
    * @param params query string params
    * @param headers request headers
    * @param credentials credentials
    * @return http response object associated with request
    */
  def getRequestWithAuth(
    uri: String,
    params: Map[String, String] = Map[String, String](),
    headers: Map[String, String] = Map[String, String](),
    credentials: String
  ): HttpResponse[String] = {
    val request: Request = if (credentials == null || credentials == "") {
      NetworkCommonsUtilities
        .buildRequest(uri, params, headers)
        .build()
    } else {
      NetworkCommonsUtilities
        .buildRequest(uri, params, headers)
        .header("Authorization", credentials)
        .build()
    }
    logger.trace(s"Built request: ${request.toString}")
    val client: OkHttpClient = new OkHttpClient()
    try {
      val response: Response = client.newCall(request).execute()
      val code = response.code()
      val message = response.message()
      val body: String = if (response.isSuccessful) {
        response.body().string()
      } else {
        ""
      }
      val content_length: Int = body.length
      val header_names: Set[String] = response.headers().names().asScala.toSet
      val headers: Map[String, String] = header_names.map(f => (f, response.headers().get(f))).toMap

      HttpResponse[String](
        response = body,
        code = code,
        headers = headers,
        message = message,
        content_length = content_length)

    } catch {
      case ex: SocketTimeoutException =>
        this.handleResponseSocketTimeoutException(ex)
      case ex: IOException =>
        this.handleResponseIOExcpetion(ex)
      case ex: Exception =>
        logger.error("Encountered exception while executing http get request.")
        logger.error("Rethrowing actual exception.")
        throw ex
    }
  }

  def postRequest(
    uri: String,
    params: Map[String, String] = Map[String, String](),
    headers: Map[String, String] = Map[String, String](),
    body: String = "",
    username: String = "",
    password: String = ""
  ): HttpResponse[String] = {
    val credentials = if (username == "" || password == "") {
      ""
    } else {
      this.generateBasicCredentials(username, password)
    }
    this.postRequestWithAuth(uri = uri, params = params, headers = headers, body = body, credentials = credentials)
  }

  def postRequestWithAuth(
    uri: String,
    params: Map[String, String] = Map[String, String](),
    headers: Map[String, String] = Map[String, String](),
    body: String,
    credentials: String
  ): HttpResponse[String] = {
    val request_body = if (body == null || body == "") {
      RequestBody.create("".getBytes())
    } else {
      RequestBody.create(body.getBytes())
    }
    val request: Request = if (credentials == null || credentials == "") {
      NetworkCommonsUtilities
        .buildRequest(uri, params, headers)
        .post(request_body)
        .build()
    } else {
      NetworkCommonsUtilities
        .buildRequest(uri, params, headers)
        .header("Authorization", credentials)
        .post(request_body)
        .build()
    }
    logger.trace(s"Built request: ${request.toString}")
    val client: OkHttpClient = new OkHttpClient()
    try {
      val response: Response = client.newCall(request).execute()
      val code = response.code()
      val message = response.message()
      val body: String = if (response.isSuccessful) {
        response.body().string()
      } else {
        ""
      }
      val content_length: Int = body.length
      val header_names: Set[String] = response.headers().names().asScala.toSet
      val headers: Map[String, String] = header_names.map(f => (f, response.headers().get(f))).toMap

      HttpResponse[String](
        response = body,
        code = code,
        headers = headers,
        message = message,
        content_length = content_length)

    } catch {
      case ex: SocketTimeoutException =>
        this.handleResponseSocketTimeoutException(ex)
      case ex: IOException =>
        this.handleResponseIOExcpetion(ex)
      case ex: Exception =>
        logger.error("Encountered exception while executing http get request.")
        logger.error("Rethrowing actual exception.")
        throw ex
    }
  }

  private def handleResponseSocketTimeoutException(ex: SocketTimeoutException): HttpResponse[String] = {
    logger.error(s"Actual exception: ${ex.getMessage}")
    HttpResponse[String](
      response = "",
      code = 408,
      headers = Map[String, String](),
      message = ex.getMessage,
      content_length = -1)
  }

  private def handleResponseIOExcpetion(ex: IOException): HttpResponse[String] = {
    logger.error(s"Actual exception: ${ex.getMessage}")
    HttpResponse[String](
      response = "",
      code = 499,
      headers = Map[String, String](),
      message = ex.getMessage,
      content_length = -1)
  }

  private def generateBasicCredentials(username: String, password: String): String =
    Credentials.basic(username, password)
}
