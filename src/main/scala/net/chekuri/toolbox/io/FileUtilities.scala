package net.chekuri.toolbox.io

import com.typesafe.scalalogging.LazyLogging

/** Singleton object containing commonly
  * used methods to perform file operations.
  */
object FileUtilities extends LazyLogging {
  /** Method to retrieve list of files present
    * in a directory.
    *
    * @param path          path to directory.
    * @param absolute_path should return absolute paths for files in directory ?
    * @throws NullPointerException     when no file is found at supplied path.
    * @throws IllegalArgumentException when supplied file is not a directory.
    * @return list of files present in the directory.
    */
  @throws[NullPointerException]
  @throws[IllegalArgumentException]
  def ls(path: String, absolute_path: Boolean = false): List[String] = {
    if (!this.isDirectory(path)) {
      throw new IllegalArgumentException(
        s"Expecting a directory, but file at supplied path : `$path` is not a directory type file.")
    }

    import java.io.File
    val dir: File = new File(path)

    val result = dir.listFiles().filter(_.isFile).toList
    if (absolute_path) {
      result.map(f => f.getAbsolutePath)
    } else {
      result.map(f => f.getName)
    }
  }

  /** Method to check if file exists.
    *
    * @param path file path.
    * @return true if file exists, false otherwise.
    */
  def exists(path: String): Boolean = {
    import java.io.File

    val file: File = new File(path)

    if (file.exists()) {
      true
    } else {
      false
    }
  }

  /** Method to check if the supplied file is a directory.
    *
    * @param path file path.
    * @throws NullPointerException when no file found at supplied file path.
    * @return returns true if file is a directory, false otherwise.
    */
  @throws[NullPointerException]
  def isDirectory(path: String): Boolean = {
    if (!this.exists(path)) {
      throw new NullPointerException(s"No file found at path: `$path`.")
    }

    import java.io.File

    val file = new File(path)
    if (file.isDirectory) {
      true
    } else {
      false
    }
  }

  /** Method to read a file as string buffer.
    *
    * @param path file path
    * @throws IllegalArgumentException when file at supplied path is a directory.
    * @return string buffer with contents of the file.
    */
  @throws[IllegalArgumentException]
  def read(path: String): StringBuffer = {
    if (this.isDirectory(path)) {
      throw new IllegalArgumentException(s"Supplied file: `$path` is a directory.")
    }
    val result: StringBuffer = new StringBuffer()
    import scala.io.Source
    val file = Source.fromFile(path)
    for (line <- file.getLines()) {
      result.append(line).append('\n')
    }
    file.close()
    result
  }

  /** Method to read a resource file as string buffer.
    *
    * @param path resource path
    * @return string buffer with contents of resource file.
    */
  def readResource(path: String): StringBuffer = {
    import scala.io.Source
    val file = Source.fromResource(path)

    val result: StringBuffer = new StringBuffer()
    for (line <- file.getLines()) {
      result.append(line)
    }

    file.close()
    result
  }
}
