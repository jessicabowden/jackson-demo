import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.util.Try

case class Book(
                 @JsonProperty("title") title: String,
                 @JsonProperty("author") author: String,
                 @JsonProperty("isbn") isbn: String
               )

class JacksonDemo extends Serializable {
  def parseJsonString(jsonStr: String): Try[Book] = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
    Try(mapper.readValue[Book](jsonStr))
  }
}



object JacksonDemo extends Serializable {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("jackson demo")

    val ss = SparkSession.builder().config(conf).getOrCreate()
    val sc = ss.sparkContext

    val demoFile = getClass.getResource("/jacksonDemoFile.json").getPath

    val bookRDD = sc.textFile(demoFile)
      .filter( line => !line.startsWith("[") && !line.startsWith("]"))
      .map { json =>
        new JacksonDemo().parseJsonString(json)
      }
      .filter(_.isSuccess)

    bookRDD.foreach(println(_))
  }
}
