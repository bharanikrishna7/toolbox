package net.chekuri.toolbox.io

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.flatspec.AnyFlatSpec

class FileUtilitiesSpec extends AnyFlatSpec with LazyLogging {
  "exists" should "return true when it's guaranteed file exists at supplied path" in {
    val path = "src/test/resources"
    val expected: Boolean = true
    val actual: Boolean = FileUtilities.exists(path)
    assert(expected == actual)
  }

  "exists" should "return false when it's guaranteed file does not exists at supplied path" in {
    val path = "src/test/invalid_fake_path"
    val expected: Boolean = false
    val actual: Boolean = FileUtilities.exists(path)
    assert(expected == actual)
  }

  "isDirectory" should "return true when file at supplied path is a directory" in {
    val path = "src/test/resources"
    val expected: Boolean = true
    val actual: Boolean = FileUtilities.isDirectory(path)
    assert(expected == actual)
  }

  "isDirectory" should "return false when file at supplied path is not a directory" in {
    val path = "src/test/resources/JsonSpecTestFile.json"
    val expected: Boolean = false
    val actual: Boolean = FileUtilities.isDirectory(path)
    assert(expected == actual)
  }

  "isDirectory" should "throw exception when no file exists at supplied path" in {
    val path = "src/test/resources/invalid_fake_path"
    try {
      FileUtilities.isDirectory(path)
      logger.error("Should have thrown exception, since no file exists at supplied path.")
      assert(false)
    } catch {
      case exception: Exception =>
        logger.info("Encountered exception message:")
        logger.info(exception.getMessage)
        logger.debug("Verifying exception message.")
        assert(exception.getMessage.contains("No file found at path"))
    }
  }

  "ls" should "correctly retrieve list of files present in supplied directory when it's guaranteed supplied path is a valid directory" in {
    val path = "src/test/resources"
    val files = FileUtilities.ls(path)
    logger.info(s" > number of files present in directory : ${files.size}")
    logger.info(" Files: ")
    logger.info("--------")
    for (i <- files.indices) {
      logger.info(s" ${i + 1}. ${files(i)}")
    }
    logger.info(" ")
    assert(files.nonEmpty)
  }

  "ls" should "correctly retrieve list of files present in supplied directory (as absolute path) when it's guaranteed supplied path is a valid directory" in {
    val path = "src/test/resources"
    val files = FileUtilities.ls(path, absolute_path = true)
    logger.info(s" > number of files present in directory : ${files.size}")
    logger.info(" Files: ")
    logger.info("--------")
    for (i <- files.indices) {
      logger.info(s" ${i + 1}. ${files(i)}")
    }
    logger.info(" ")
    assert(files.nonEmpty)
    logger.debug("Verifying absolute path contains the initial directory path we supplied.")
    assert(files.last.contains(path))
  }

  "ls" should "correctly throw exception when no file exists at supplied path" in {
    val path = "src/test/invalid_fake_path"
    try {
      FileUtilities.ls(path)
      logger.error("Should have thrown exception since it's guaranteed that no file at supplied path exists.")
      assert(false)
    } catch {
      case exception: Exception =>
        logger.info("Encountered exception as expected.")
        logger.info("Actual exception message:")
        logger.info(exception.getMessage)
        logger.debug("Verifying exception message.")
        assert(exception.getMessage.contains("No file found at path"))
    }
  }

  "ls" should "correctly throw exception when no file at supplied path is not a directory" in {
    val path = "src/test/resources/JsonSpecTestFile.json"
    try {
      FileUtilities.ls(path)
      logger.error("Should have thrown exception since it's guaranteed that no file at supplied path exists.")
      assert(false)
    } catch {
      case exception: Exception =>
        logger.info("Encountered exception as expected.")
        logger.info("Actual exception message:")
        logger.info(exception.getMessage)
        logger.debug("Verifying exception message.")
        assert(exception.getMessage.contains("Expecting a directory, but file at supplied path"))
    }
  }

  "read" should "correctly deserialize a file into string buffer" in {
    val path = "src/test/resources/FileUtilitiesResource.txt"
    val expected: String =
      """|test
         |test_line_2
         |""".stripMargin
    val actual: String = FileUtilities.read(path).toString
    logger.info(s"File contents: $actual")
    logger.debug("Ensuring the file content is as expected.")
    assert(expected == actual)
  }

  "read" should "throw exception when file at supplied path is a directory" in {
    val path = "src/test/resources"
    try {
      FileUtilities.read(path)
      logger.error("Should have thrown exception since it's guaranteed that file at supplied path is a directory.")
      assert(false)
    } catch {
      case exception: Exception =>
        logger.info("Encountered exception as expected.")
        logger.info("Actual exception message:")
        logger.info(exception.getMessage)
        logger.debug("Verifying exception message.")
        assert(exception.getMessage.contains(" is a directory"))
    }
  }

  "read" should "correctly throw exception when no file exists at supplied path" in {
    val path = "src/test/resources/invalid_fake_path"
    try {
      FileUtilities.read(path)
      logger.error("Should have thrown exception since it's guaranteed that no file at supplied path exists.")
      assert(false)
    } catch {
      case exception: Exception =>
        logger.info("Encountered exception as expected.")
        logger.info("Actual exception message:")
        logger.info(exception.getMessage)
        logger.debug("Verifying exception message.")
        assert(exception.getMessage.contains("No file found at path"))
    }
  }
}
