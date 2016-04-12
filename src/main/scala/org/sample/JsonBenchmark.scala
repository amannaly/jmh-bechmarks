package org.sample

import java.util.concurrent.TimeUnit
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization

import play.api.libs.functional.syntax._
import play.api.libs.json._

import org.openjdk.jmh.annotations._

import scala.util.Random


/**
 * Performance of both Json4s and Play are comparable when we have a
 * simple case class. But if there are nested case classes Play is much faster.
 *
 * with nested class (RestResponse containing JsonTest.)
 * Benchmark                        Mode  Cnt    Score    Error  Units
 * JsonBenchmark.testJson4sWrite    avgt   20  479.076 ± 22.311  ms/op
 * JsonBenchmark.testPlayJsonWrite  avgt   20  238.980 ±  8.750  ms/op
 *
 *
 * Benchmark                       Mode  Cnt    Score     Error  Units
 *  JsonBenchmark.testJson4sRead    avgt   20  792.841 ± 107.084  ms/op
 *  JsonBenchmark.testPlayJsonRead  avgt   20  954.414 ±  84.503  ms/op
 */
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
class JsonBenchmark {

  private val string = scala.io.Source.fromFile("test.json").mkString
  private implicit val formats = DefaultFormats
  private val list = (1 to 100000).map { x=>
    val size = Random.nextInt(10)
    val json = JsonTest(Random.nextInt(), Random.nextDouble(), Random.nextString(size))
    Container(
      version = 10,
      data = json,
      status = "Ok",
      total_count = 100000,
      offset = 0
    )
  }.toList

  private implicit val reader: Reads[JsonTest] = (
    (JsPath \ "index").read[Int] and
      (JsPath \ "value").read[Double] and
      (JsPath \ "name").read[String]
    )(JsonTest.apply _)

  private implicit val responseReader: Reads[Container[JsonTest]] = (
    (JsPath \ "version").read[Int] and
      (JsPath \ "data").read[JsonTest] and
      (JsPath \ "status").read[String] and
      (JsPath \ "total_count").read[Int] and
      (JsPath \ "offset").read[Int]
    )(Container.apply[JsonTest] _)

  private implicit val writer = new Writes[JsonTest] {
    def writes(c: JsonTest): JsValue = {
      Json.obj(
        "index" -> c.index,
        "value" -> c.value,
        "name" -> c.name
      )
    }
  }

  private implicit val responseWriter = new Writes[Container[JsonTest]] {
    def writes(c: Container[JsonTest]): JsValue = {
      Json.obj(
        "version" -> c.version,
        "data" -> c.data,
        "status" -> c.status,
        "total_count" -> c.total_count,
        "offset" -> c.offset
      )
    }
  }

  @Benchmark
  def testJson4sRead(): List[Container[JsonTest]] = {
    val list = parse(string).extract[List[Container[JsonTest]]]
    list
  }

//  @Benchmark
//  def testJson4sWrite(): String = {
//    val string = Serialization.write(list)
//    string
//  }
//
//  @Benchmark
//  def testPlayJsonWrite(): String = {
//    val string = Json.toJson(list).toString()
//    string
//  }

  @Benchmark
  def testPlayJsonRead(): List[Container[JsonTest]] = {
    val list = Json.parse(string).as[List[Container[JsonTest]]]
    list
  }

}
