package org.sample

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Benchmark, _}

/**
 * Adding 1000 key, value pairs to the Map
 *
 * Benchmark                           Mode  Cnt       Score    Error       Units
 *  MapBenchMark.testImmutableMap      avgt   20  114364.004 ± 2363.975     ns/op
 *  MapBenchMark.testJavaMap           avgt   20     22189.336 ± 259.899    ns/op
 *  MapBenchMark.testMutableMap        avgt   20     41996.687 ± 1369.807   ns/op
 */
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class MapBenchMark {

  private case class Entry(key: Int, value: String)

  private val entries = (1 to 1000).map(x => Entry(x, x.toString)).toList
  private var javaMap = new java.util.HashMap[Int, String]
  private var mutableMap = scala.collection.mutable.Map.empty[Int, String]
  private var immutableMap = Map.empty[Int, String]
  private var entriesIterator = entries.iterator

  @Setup(Level.Invocation)
  def setUp(): Unit = {
    javaMap = new java.util.HashMap[Int, String]
    mutableMap = scala.collection.mutable.Map.empty[Int, String]
    immutableMap = Map.empty[Int, String]
    entriesIterator = entries.iterator
  }

  @Benchmark
  def testJavaMap(): java.util.HashMap[Int, String] = {
    var entry: Entry = null
    while (entriesIterator.hasNext) {
      entry = entriesIterator.next()
      javaMap.put(entry.key, entry.value)
    }
    javaMap
  }

  @Benchmark
  def testMutableMap(): scala.collection.mutable.Map[Int, String] = {
    var entry: Entry = null
    while (entriesIterator.hasNext) {
      entry = entriesIterator.next()
      mutableMap.put(entry.key, entry.value)
    }
    mutableMap
  }

  @Benchmark
  def testImmutableMap(): Map[Int, String] = {
    var entry: Entry = null
    while (entriesIterator.hasNext) {
      entry = entriesIterator.next()
      immutableMap += (entry.key -> entry.value)
    }
    immutableMap
  }
}
