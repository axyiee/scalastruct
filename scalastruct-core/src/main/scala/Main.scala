import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import dev.axyria.scalastruct.*
import dev.axyria.scalastruct.given

object Main extends IOApp.Simple {
  val run =
    UnsafeOffHeapArray[IO, Int](1024)
      .flatTap { _ => IO.println("> arr.set(0, 6)") }
      .flatTap { arr => arr.set(0, 6) }
      .flatTap { _ => IO.println("> arr.get(0)") }
      .flatTap { arr => arr.get(0).flatMap(IO.println) }
      .flatTap { _ => IO.println("> arr.set(8, 2356)") }
      .flatTap { arr => arr.set(8, 2356) }
      .flatTap { _ => IO.println("> arr.get(8)") }
      .flatTap { arr => arr.get(8).flatMap(IO.println) }
      .flatTap { _ => IO.println("> arr.set(3, 912)") }
      .flatTap { arr => arr.set(3, 912) }
      .flatTap { _ => IO.println("> arr.get(3)") }
      .flatTap { arr => arr.get(3).flatMap(IO.println) }
      .flatMap { res => IO.println(res) }
      .void
}
