package dev.axyria.scalastruct

import java.lang.reflect.Modifier
import sun.misc.Unsafe
import util.chaining.scalaUtilChainingOps

/** The unsafe operator used behind the scenes. Since different runtimes use different names for the
  * [[Unsafe]] instance retriever, we can't use a reflected 'theUnsafe' declared field, instead we walk
  * through all declared fields by using Reflection then choosing the one who returns an [[Unsafe]] instance.
  */
given jvmUnsafe: Unsafe =
  try {
    classOf[Unsafe].getDeclaredFields
      .find(f =>
        Modifier.isStatic(f.getModifiers) && Modifier.isFinal(f.getModifiers) && f.getType == classOf[Unsafe]
      )
      .get
      .tap(_.setAccessible(true))
      .get(null)
      .asInstanceOf[Unsafe]
  } catch {
    case aux: Throwable => throw RuntimeException("sun.misc.Unsafe isn't available.", aux)
  }

//noinspection ScalaRedundantConversion
given jvmUnsafePointerFactory(using unsafe: Unsafe): UnsafePointerFactory with
  def alloc(size: Long): Pointer =
    Pointer(unsafe.allocateMemory(size))

  def realloc[A <: Primitive](address: Pointer, data: A)(using p: PrimitiveMemoryFactory[A]): Unit =
    p.write(address, data)

  def reallocBlock(address: Pointer, length: Long) =
    address.tap(addr => unsafe.reallocateMemory(addr.toLong, length))

  def freePointer(address: Pointer): Unit =
    unsafe.freeMemory(address.toLong)

  def visit[A <: Primitive](address: Pointer)(using p: PrimitiveMemoryFactory[A]): A =
    p.read(address)
