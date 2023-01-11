package dev.axyria.scalastruct

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Warmup
import scala.collection.mutable.ArrayBuffer

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class UnsafeArrayBenchmark {
  def capacity = 1024

  @Benchmark
  @Fork(value = 2)
  @Warmup(iterations = 10)
  @Measurement(iterations = 10)
  def unsafeAllocationSpeed: Unit =
    val arr = UnsafeOffHeapArray[Int](capacity)
    for (i <- 0 to capacity - 1) yield {
      arr.set(i, i)
    }

  @Benchmark
  @Fork(value = 2)
  @Warmup(iterations = 10)
  @Measurement(iterations = 10)
  def stdlibAllocationSpeed: Unit =
    val arr = ArrayBuffer.fill[Int](capacity)(0)
    for (i <- 0 to capacity - 1) yield {
      arr(i) = i
    }
}
