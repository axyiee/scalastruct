package dev.axyria.scalastruct

import cats.effect.kernel.Sync
import cats.implicits.*
import cats.syntax.all.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

private inline def addrIndex(address: Pointer, index: Long, alignment: Int): Pointer =
  Pointer(address.toLong + index * alignment)

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
      .delay(addrIndex(address, index, alignment))
      .flatTap(addr => f.realloc[Int](addr, data))

  override def alignment: Int = Integer.BYTES

  override def read(addr: Pointer): F[Int] =
    f.visit[Int](addr)

given unsafeCharKind[F[_]: Sync](using f: UnsafePointerFactory[F]): UnsafeStructKind[F, Char] with
  inline def arrayOf(value: Char): Array[Byte] =
    Array((value >>> 8).toByte, value.toByte)

  override def alloc(data: Char): F[Pointer] = allocEmpty(alignment)
    .flatTap(addr => store(addr, 0, data))

  override def allocEmpty(length: Long): F[Pointer] = f.alloc(length * alignment)

  override def store(address: Pointer, index: Long, data: Char): F[Pointer] =
    Sync[F]
      .delay(addrIndex(address, index, alignment))
      .flatTap(addr => f.realloc[Char](addr, data))

  override def alignment: Int = Character.BYTES

  override def read(addr: Pointer): F[Char] =
    f.visit[Char](addr)
