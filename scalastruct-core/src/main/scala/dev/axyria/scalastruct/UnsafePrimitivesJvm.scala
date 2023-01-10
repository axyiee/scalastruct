package dev.axyria.scalastruct

import cats.effect.Sync
import cats.syntax.all.*
import sun.misc.Unsafe

given jvmByteMemoryFactory[F[_]: Sync](using unsafe: F[Unsafe]): PrimitiveMemoryFactory[F, Byte] with
  def write(address: Pointer, data: Byte): F[Unit] = unsafe.map(_.putByte(address.toLong, data))
  def read(address: Pointer): F[Byte]              = unsafe.map(_.getByte(address.toLong))

given jvmIntMemoryFactory[F[_]: Sync](using unsafe: F[Unsafe]): PrimitiveMemoryFactory[F, Int] with
  def write(address: Pointer, data: Int): F[Unit] = unsafe.map(_.putInt(address.toLong, data))
  def read(address: Pointer): F[Int]              = unsafe.map(_.getInt(address.toLong))

given jvmLongMemoryFactory[F[_]: Sync](using unsafe: F[Unsafe]): PrimitiveMemoryFactory[F, Long] with
  def write(address: Pointer, data: Long): F[Unit] = unsafe.map(_.putLong(address.toLong, data))
  def read(address: Pointer): F[Long]              = unsafe.map(_.getLong(address.toLong))

given jvmCharMemoryFactory[F[_]: Sync](using unsafe: F[Unsafe]): PrimitiveMemoryFactory[F, Char] with
  def write(address: Pointer, data: Char): F[Unit] = unsafe.map(_.putChar(address.toLong, data))
  def read(address: Pointer): F[Char]              = unsafe.map(_.getChar(address.toLong))

given jvmDoubleMemoryFactory[F[_]: Sync](using unsafe: F[Unsafe]): PrimitiveMemoryFactory[F, Double] with
  def write(address: Pointer, data: Double): F[Unit] = unsafe.map(_.putDouble(address.toLong, data))
  def read(address: Pointer): F[Double]              = unsafe.map(_.getDouble(address.toLong))

given jvmFloatMemoryFactory[F[_]: Sync](using unsafe: F[Unsafe]): PrimitiveMemoryFactory[F, Float] with
  def write(address: Pointer, data: Float): F[Unit] = unsafe.map(_.putFloat(address.toLong, data))
  def read(address: Pointer): F[Float]              = unsafe.map(_.getFloat(address.toLong))

given jvmShortMemoryFactory[F[_]: Sync](using unsafe: F[Unsafe]): PrimitiveMemoryFactory[F, Short] with
  def write(address: Pointer, data: Short): F[Unit] = unsafe.map(_.putShort(address.toLong, data))
  def read(address: Pointer): F[Short]              = unsafe.map(_.getShort(address.toLong))
