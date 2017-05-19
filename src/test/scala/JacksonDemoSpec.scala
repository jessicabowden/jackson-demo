import org.scalatest.{FlatSpec, Matchers}
import scala.util.{Failure, Try}

class JacksonDemoSpec extends FlatSpec with Matchers {
  val jacksonDemo: JacksonDemo = new JacksonDemo()

  "Given JacksonDemo, and a blank string" should "return a failed try" in {
    val parsedString = jacksonDemo.parseJsonString("")
    parsedString.isFailure should be (true)
  }

  "Given JacksonDemo, and a single valid JSON string" should "return a successful try" in {
    val rawString = "{\"title\": \"Apocalypso\", \"author\": \"Robert Rankin\", \"isbn\": \"978-0552145893\"}"
    val parsedString = jacksonDemo.parseJsonString(rawString)
    parsedString.isSuccess should be (true)
  }

  "Given JacksonDemo, and a single valid JSON string" should "return a Book object inside of a Try" in {
    val rawString = "{\"title\": \"Apocalypso\", \"author\": \"Robert Rankin\", \"isbn\": \"978-0552145893\"}"

    val expectedResult = Try(
      Book("Apocalypso", "Robert Rankin", "978-0552145893")
    )

    jacksonDemo.parseJsonString(rawString) should be (expectedResult)
  }

  "Given JacksonDemo, and yielding a list of valid JSON strings" should "return a List of Try[Book] objects" in {
    val rawStrings = List(
      "{\"title\": \"Apocalypso\", \"author\": \"Robert Rankin\", \"isbn\": \"978-0552145893\"}",
      "{\"title\": \"Apocalypso\", \"author\": \"Robert Rankin\", \"isbn\": \"978-0552145893\"}",
      "{\"title\": \"Apocalypso\", \"author\": \"Robert Rankin\", \"isbn\": \"978-0552145893\"}"
    )

    val parsedStrings = for (rawString <- rawStrings)
      yield jacksonDemo.parseJsonString(rawString)

    val expectedResult = List(
      Try(Book("Apocalypso", "Robert Rankin", "978-0552145893")),
      Try(Book("Apocalypso", "Robert Rankin", "978-0552145893")),
      Try(Book("Apocalypso", "Robert Rankin", "978-0552145893"))
    )
  }

  "Given JacksonDemo, and a completely invalid string" should "return a failed result" in {
    val invalidString = "badlfkjgdlk"
    jacksonDemo.parseJsonString(invalidString).isFailure should be(true)
  }

  "Given JacksonDemo, and an invalid json string" should "return a failed result" in {
    val invalidJsonString = "{\"name\": \"Jim\", \"dob\": \"050687\"}"
    jacksonDemo.parseJsonString(invalidJsonString).isSuccess should be (false)
  }

  "Given JacksonDemo, retrieving the variables" should "return the expected fields" in {
    val rawString = "{\"title\": \"Apocalypso\", \"author\": \"Robert Rankin\", \"isbn\": \"978-0552145893\"}"
    val expectedResult = Try(
      Book("Apocalypso", "Robert Rankin", "978-0552145893")
    )

    jacksonDemo.parseJsonString(rawString).get.author should be ("Robert Rankin")
  }
}
