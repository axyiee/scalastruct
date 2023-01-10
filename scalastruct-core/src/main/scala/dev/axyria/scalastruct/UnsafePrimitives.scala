package dev.axyria.scalastruct

import cats.effect.kernel.Sync
import cats.implicits.*
import cats.syntax.all.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

given unsafeByteKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Byte] with
  override def alloc(value: Byte): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, value))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length)

  override def store(address: Pointer, index: Long, data: Byte): F[Pointer] =
    Sync[F]
      .delay(address + index)
      .flatTap(_ => f.realloc[Byte](address + index, data))

  override def alignment: Int = java.lang.Byte.BYTES

  override def read(addr: Pointer): F[Byte] = f.visit(addr)

given unsafeIntKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Int] with
  override def alloc(data: Int): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, data))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length * alignment)

  override def store(address: Pointer, index: Long, data: Int): F[Pointer] =
    Sync[F]
      .delay(Pointer.buildAddress(address, index, alignment))
      .flatTap(addr => f.realloc[Int](addr, data))

  override def alignment: Int = Integer.BYTES

  override def read(addr: Pointer): F[Int] =
    f.visit[Int](addr)

given unsafeCharKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Char] with
  override def alloc(data: Char): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, data))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length * alignment)

  override def store(address: Pointer, index: Long, data: Char): F[Pointer] =
    Sync[F]
      .delay(Pointer.buildAddress(address, index, alignment))
      .flatTap(addr => f.realloc[Char](addr, data))

  override def alignment: Int = Character.BYTES

  override def read(addr: Pointer): F[Char] =
    f.visit[Char](addr)

given unsafeLongKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Long] with
  override def alloc(data: Long): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, data))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length * alignment)

  override def store(address: Pointer, index: Long, data: Long): F[Pointer] =
    Sync[F]
      .delay(Pointer.buildAddress(address, index, alignment))
      .flatTap(addr => f.realloc[Long](addr, data))

  override def alignment: Int = java.lang.Long.BYTES

  override def read(addr: Pointer): F[Long] =
    f.visit[Long](addr)

given unsafeDoubleKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Double] with
  override def alloc(data: Double): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, data))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length * alignment)

  override def store(address: Pointer, index: Long, data: Double): F[Pointer] =
    Sync[F]
      .delay(Pointer.buildAddress(address, index, alignment))
      .flatTap(addr => f.realloc[Double](addr, data))

  override def alignment: Int = java.lang.Double.BYTES

  override def read(addr: Pointer): F[Double] =
    f.visit[Double](addr)

given unsafeFloatKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Float] with
  override def alloc(data: Float): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, data))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length * alignment)

  override def store(address: Pointer, index: Long, data: Float): F[Pointer] =
    Sync[F]
      .delay(Pointer.buildAddress(address, index, alignment))
      .flatTap(addr => f.realloc[Float](addr, data))

  override def alignment: Int = java.lang.Float.BYTES

  override def read(addr: Pointer): F[Float] =
    f.visit[Float](addr)

given unsafeShortKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Short] with
  override def alloc(data: Short): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, data))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length * alignment)

  override def store(address: Pointer, index: Long, data: Short): F[Pointer] =
    Sync[F]
      .delay(Pointer.buildAddress(address, index, alignment))
      .flatTap(addr => f.realloc[Short](addr, data))

  override def alignment: Int = java.lang.Short.BYTES

  override def read(addr: Pointer): F[Short] =
    f.visit[Short](addr)
