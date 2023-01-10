package dev.axyria.scalastruct

import cats.effect.Sync
import cats.syntax.all.*
import java.lang.reflect.Modifier
import sun.misc.Unsafe

/** The unsafe operator used behind the scenes. Since different runtimes use different names for the
  * [[Unsafe]] instance retriever, we can't use a reflected 'theUnsafe' declared field, instead we walk
  * through all declared fields by using Reflection then choosing the one who returns an [[Unsafe]] instance.
  */
given jvmUnsafe[F[_]: Sync]: F[Unsafe] =
  Sync[F]
    .delay(classOf[Unsafe])
    .map(_.getDeclaredFields)
    .map(_.filter(_.getType == classOf[Unsafe]))
    .map(
      _.find(f => Modifier.isStatic(f.getModifiers) && Modifier.isFinal(f.getModifiers)).head
    )
    .flatTap(f => Sync[F].delay(f.setAccessible(true)))
    .map(_.get(null).asInstanceOf[Unsafe])
    .handleErrorWith(_ =>
      Sync[F]
        .raiseError(new RuntimeException("sun.misc.Unsafe isn't available."))
    )

//noinspection ScalaRedundantConversion
given jvmUnsafePointerFactory[F[_]: Sync](using unsafe: F[Unsafe]): UnsafePointerFactory[F] with
  def alloc(size: Long): F[Pointer] =
    unsafe.flatMap(u => Sync[F].delay(u.allocateMemory(size)).map(Pointer.apply))

  def realloc[A <: Primitive](address: Pointer, data: A)(using p: PrimitiveMemoryFactory[F, A]): F[Unit] =
    p.write(address, data)

  def freePointer(address: Pointer): F[Unit] =
    unsafe.flatMap(u => Sync[F].delay(u.freeMemory(address.toLong)))

  def visit[A <: Primitive](address: Pointer)(using p: PrimitiveMemoryFactory[F, A]): F[A] =
    p.read(address)
