package dev.axyria.scalastruct

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.*
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Warmup

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class UnsafeArrayBenchmark {
  def capacity = 1024 * 1024

  @Benchmark
  @Fork(value = 2)
  @Warmup(iterations = 10)
  @Measurement(iterations = 10)
  def scalastruct: Unit =
    UnsafeOffHeapArray[IO, Double](capacity)
      .map { arr =>
        (0 to capacity - 1).toList map { i =>
          arr
            .set(i, capacity * 2)
            .flatMap(_ =>
              arr
                .get(i)
                .flatMap(v =>
                  if v == (capacity * 2) then IO.unit
                  else IO.raiseError(new Exception(s"Invalid value. Expected ${capacity * 2} got $v"))
                )
            )
        }
      }
      .flatMap(_.sequence)
      .unsafeRunSync()

  @Benchmark
  @Fork(value = 2)
  @Warmup(iterations = 10)
  @Measurement(iterations = 10)
  def stdlib: Unit =
    IO.delay(Array.fill[Double](capacity)(0.0))
      .map { arr =>
        (0 to capacity - 1).toList map { i =>
          IO.delay(arr(i) = capacity * 2)
            .flatMap(_ =>
              IO.delay(arr(i))
                .flatMap(v =>
                  if v == (capacity * 2) then IO.unit
                  else IO.raiseError(new Exception(s"Invalid value. Expected ${capacity * 2} got $v"))
                )
            )
        }
      }
      .flatMap(_.sequence)
      .unsafeRunSync()
}
