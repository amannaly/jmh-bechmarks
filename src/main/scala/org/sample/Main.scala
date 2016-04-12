package org.sample

import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder

object Main {

  def main(args: Array[String]) {

    val opt = new OptionsBuilder()
      .include(classOf[MapBenchMark].getSimpleName)
      .forks(1)
      .build()

    new Runner(opt).run()
  }
}
