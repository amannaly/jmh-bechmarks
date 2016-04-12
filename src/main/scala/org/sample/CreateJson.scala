package org.sample

import java.io.{BufferedWriter, FileWriter}

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write

import scala.util.Random

/**
 * Creates a test.json file
 */
object CreateJson {

  def main(args: Array[String]) {

    val list = (1 to 100000).map { x =>
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

    implicit val formats = Serialization.formats(NoTypeHints)
    val string = pretty(parse(write(list)))

    val buffer = new BufferedWriter(new FileWriter("test.json"))
    buffer.write(string)
    buffer.close()
  }
}


case class JsonTest(index: Int, value: Double, name: String)
case class Inner(foo: Long, ids: List[Double])

case class Container[T] (
  version: Int,
  data: T,
  status: String,
  total_count: Int,
  offset: Int)
