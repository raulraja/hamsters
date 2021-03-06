import io.github.hamsters.Validation
import Validation._
import org.scalatest._

class ValidationSpec extends FlatSpec with Matchers {

  "Validation" should "give no failures" in {
    val e1 = Right(1)
    val e2 = Right(2)
    val e3 = Right(3)

    Validation.failures(e1,e2,e3) should be(Nil)
  }

  "Validation" should "give failures" in {
    val e1 = Right(1)
    val e2 = Left("nan")
    val e3 = Left("nan2")

    Validation.failures(e1,e2,e3) should be(List("nan", "nan2"))
  }

  "Validation" should "give failures with mixed types" in {
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan")

    Validation.failures(e1, e2, e3) should be(List("nan"))
  }

  "Validation" should "give values if all OK" in {
    val e1 = Right(1)
    val e2 = Right("2")

    Validation.result(e1, e2) should be(Right((1,"2")))
  }

  "Validation" should "not give values if all are not OK" in {
    val e1 = Right(1)
    val e2 = Right("2")
    val e3 = Left("nan")

    Validation.result(e1, e2, e3) should be(Left(List("nan")))
  }


  "OK" should "give a value using get and getOrElse" in {
    val e = Right(1)
    e.get should be(1)
    e.getOrElse(2) should be(1)
  }

  "KO" should "give a value using getOrElse" in {
    val e = Left("d'oh!")
    e.getOrElse(2) should be(2)
  }

  "Either" should "compose using flatMap and map" in {
    val e1 = Right(1)
    val e2 = Right(2)
    val e3 = Right(3)

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield s"$v1-$v2-$v3"

    combine should be(Right("1-2-3"))

  }

  "Either" should "stop at first error" in {

    val e1: Either[String, Int] = Right(1)
    val e2: Either[String, Int] = Left("nan")
    val e3: Either[String, Int] = Left("nan2")

    val combine = for {
      v1 <- e1
      v2 <- e2
      v3 <- e3
    } yield s"$v1-$v2-$v3"

    combine should be(Left("nan"))

  }

  "from catchable" should "convert to either" in {

    def compute(x: BigInt) = BigInt(2)/x

    fromCatchable(compute(1)) should be(Right(BigInt(2)))
    fromCatchable(compute(0)) should be(Left("BigInteger divide by zero"))

    fromCatchable(compute(0), (t: Throwable) => t.getClass.getSimpleName) should be(Left("ArithmeticException"))

  }

}