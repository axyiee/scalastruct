package dev.axyria.scalastruct

import sun.misc.Unsafe

given jvmByteMemoryFactory(using unsafe: Unsafe): PrimitiveMemoryFactory[Byte] with
  def write(address: Pointer, data: Byte): Unit = unsafe.putByte(address.toLong, data)
  def read(address: Pointer): Byte              = unsafe.getByte(address.toLong)

given jvmIntMemoryFactory(using unsafe: Unsafe): PrimitiveMemoryFactory[Int] with
  def write(address: Pointer, data: Int): Unit = unsafe.putInt(address.toLong, data)
  def read(address: Pointer): Int              = unsafe.getInt(address.toLong)

given jvmLongMemoryFactory(using unsafe: Unsafe): PrimitiveMemoryFactory[Long] with
  def write(address: Pointer, data: Long): Unit = unsafe.putLong(address.toLong, data)
  def read(address: Pointer): Long              = unsafe.getLong(address.toLong)

given jvmCharMemoryFactory(using unsafe: Unsafe): PrimitiveMemoryFactory[Char] with
  def write(address: Pointer, data: Char): Unit = unsafe.putChar(address.toLong, data)
  def read(address: Pointer): Char              = unsafe.getChar(address.toLong)

given jvmDoubleMemoryFactory(using unsafe: Unsafe): PrimitiveMemoryFactory[Double] with
  def write(address: Pointer, data: Double): Unit = unsafe.putDouble(address.toLong, data)
  def read(address: Pointer): Double              = unsafe.getDouble(address.toLong)

given jvmFloatMemoryFactory(using unsafe: Unsafe): PrimitiveMemoryFactory[Float] with
  def write(address: Pointer, data: Float): Unit = unsafe.putFloat(address.toLong, data)
  def read(address: Pointer): Float              = unsafe.getFloat(address.toLong)

given jvmShortMemoryFactory(using unsafe: Unsafe): PrimitiveMemoryFactory[Short] with
  def write(address: Pointer, data: Short): Unit = unsafe.putShort(address.toLong, data)
  def read(address: Pointer): Short              = unsafe.getShort(address.toLong)
