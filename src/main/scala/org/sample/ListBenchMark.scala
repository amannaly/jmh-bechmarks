package org.sample

import java.util
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Benchmark, _}
import scala.util.Random

/**
 * Adding 1000 elements to the list.
 *
 * Benchmark                               Mode  Cnt    Score    Error    Units
 *  ListBenchMark.testImmutableList        avgt   20  9897.755 ± 427.546  ns/op
 *  ListBenchMark.testJavaList             avgt   20  8553.295 ±  92.438  ns/op
 *  ListBenchMark.testMutableList          avgt   20  8688.939 ± 136.757  ns/op
 */
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class ListBenchMark {

  private var immutableList = List.empty[Int]
  private var mutableList = scala.collection.mutable.ListBuffer.empty[Int]
  private var javaList: java.util.ArrayList[Int] = new util.ArrayList[Int]()
  private val entries = (1 to 1000).map(x => Random.nextInt()).toList
  private var entriesIterator = entries.iterator

  /**
   * This ensures that we clear the list before each invocation of
   * the benchmark method.
   * A simpler alternative is to do the initialization in the
   * benchmark method itself, but then we include the initialization
   * time in the benchmark.
   */
  @Setup(Level.Invocation)
  def setUp(): Unit = {
    javaList = new util.ArrayList[Int]()
    immutableList = List.empty[Int]
    mutableList = scala.collection.mutable.ListBuffer.empty[Int]
    entriesIterator = entries.iterator
  }


  @Benchmark
  def testImmutableList(): List[Int] = {
    while (entriesIterator.hasNext) {
      immutableList = entriesIterator.next() :: immutableList
    }
    immutableList
  }

  @Benchmark
  def testMutableList(): scala.collection.mutable.ListBuffer[Int] = {
    while (entriesIterator.hasNext) {
      mutableList += entriesIterator.next()
    }
    mutableList
  }

  @Benchmark
  def testJavaList(): util.ArrayList[Int] = {
    while (entriesIterator.hasNext)
      javaList.add(entriesIterator.next())
    javaList
  }
}
