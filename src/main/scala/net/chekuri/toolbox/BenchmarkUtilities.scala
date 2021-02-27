package net.chekuri.toolbox

import com.typesafe.scalalogging.LazyLogging
import net.chekuri.toolbox.JsonUtilities.JsonTraits

import scala.concurrent.{ExecutionContext, Future}

object BenchmarkUtilities extends LazyLogging {

  /** Object to hold the results of benchmark
    * task | operation.
    *
    * @param result                        result of benchmark task
    * @param execution_time_in_nanoseconds time taken to execute task in nanoseconds
    * @tparam R expected result type
    */
  case class BenchmarkResult[R](result: R, execution_time_in_nanoseconds: Long) extends JsonTraits

  /** Method to execute a function | code block
    * and time it's performance.
    *
    * NOTE:
    *
    * This method will not correctly compute execution time
    * for asynchronous methods.
    *
    * @param block function | code block to execute
    * @tparam R expected result type
    * @return benchmark result object with result of block and execution time in nanoseconds.
    */
  def run[R](block: => R): BenchmarkResult[R] = {
    import java.lang.System.nanoTime
    val start: Long = nanoTime()
    val result = block
    val end: Long = nanoTime()
    val execution_time_in_nanoseconds = end - start
    logger.trace(s"Time taken to execute operation : $execution_time_in_nanoseconds")
    BenchmarkResult(result, execution_time_in_nanoseconds)
  }

  /** Method to execute an asynchronous function | code block
    * and time it's performance.
    *
    * @param block function | code block to execute
    * @param ec    execution context to be used with future
    * @tparam R expected result type
    * @return future benchmark result object with result of block and execution time in nanoseconds.
    */
  def runAsync[R](block: => Future[R], ec: ExecutionContext): Future[BenchmarkResult[R]] = {
    import java.lang.System.nanoTime
    val start: Long = nanoTime()
    block.map(f => {
      val result = f
      val end: Long = nanoTime()
      val execution_time_in_nanoseconds = end - start
      logger.debug(s"Time taken to execute operation : $execution_time_in_nanoseconds")
      BenchmarkResult(result, execution_time_in_nanoseconds)
    })(ec)
  }
}
