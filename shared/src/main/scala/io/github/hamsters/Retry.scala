package io.github.hamsters

import scala.util.{Try, Success, Failure}

object Retry {
 
  /**
   * Retry a function several times
   * @param maxTries number of retries
   * @param errorFn the function to run if it fails, to handle the error message
   * @param fn the function to run
   * @tparam T
   * @return Try of result
   */
  def apply[T](maxTries: Int, errorFn: (String) => Unit = _=> Unit)(fn: => T): Try[T] = {
    @annotation.tailrec
    def retry(nbTries: Int, errorFn: (String) => Unit)(fn: => T): Try[T] = {
      Try {fn} match {
        case Success(x) => Success(x)
        case _ if nbTries > 1 => retry(nbTries - 1, errorFn)(fn)
        case Failure(e) =>
          errorFn(s"Tried $maxTries times, still not enough : ${e.getMessage}")
          Failure(e)
      }
    }
    retry(maxTries,errorFn)(fn)
  }
}
