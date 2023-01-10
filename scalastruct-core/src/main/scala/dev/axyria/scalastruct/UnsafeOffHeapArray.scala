package dev.axyria.scalastruct

import cats.effect.Sync
import cats.syntax.all.*
import dev.axyria.scalastruct.UnsafeOffHeapArray.offset

/** A simple implementation of a fixed-length off-heap - using native memory not managed by the garbage
  * collector - array.
  *
  * @author
  *   Pedro H.
  * @param length
  *   The length of this array.
  * @tparam F
  *   The monoid where the computations will be performed.
  * @tparam A
  *   The type of the elements of the array.
  * @author
  *   Pedro H.
  */
case class UnsafeOffHeapArray[F[_]: Sync, A](address: Pointer, length: Long)(using
    kind: UnsafeStructKind[F, A]
) {

  /** Modifies the memory stack to add an element to this array.
    *
    * @param index
    *   The index of the element to be added or replaced.
    * @param value
    *   The value matching the given index.
    * @return
    *   A computation of a memory reallocating.
    */
  def set(index: Long, value: A): F[Pointer] =
    kind.store(address, index, value)

  /** Retrieves a value from this off-heap array directly from the memory stack.
    *
    * @param index
    *   The index of the element to be retrieved.
    * @return
    *   A computation of memory value retrieving then converting it to [A].
    */
  def get(index: Long): F[A] =
    kind.read(address + index * offset[F, A])

  /** Frees/invalidates this array.
    *
    * @return
    *   A computation of a memory deallocation process.
    */
  def free(): F[Unit] =
    address.free()
}

object UnsafeOffHeapArray {

  /** Initializes this array by allocating the size of all elements expected to be in this array.
    *
    * @param length
    *   The length of this array.
    * @return
    *   A computation of a full array memory allocation followed by a new helper class for managing unsafe off
    *   heap arrays with ease.
    */
  def apply[F[_]: Sync, A](length: Long)(using kind: UnsafeStructKind[F, A]): F[UnsafeOffHeapArray[F, A]] =
    Sync[F]
      .delay(length)
      .flatMap(l => if l <= 0 then invalidLengthErr[F, Pointer] else kind.allocEmpty(l))
      .map(addr => UnsafeOffHeapArray(addr, length))

  private inline def invalidLengthErr[F[_]: Sync, A]: F[A] =
    Sync[F].raiseError(
      new IllegalArgumentException("The length of the unsafe off-heap array must be greater than 0.")
    )

  /** The offset between the start of each element. This expects all elements to be primitives or simply have
    * a fixed size (non-dynamic) size between all elements.
    */
  inline def offset[F[_], A](using kind: UnsafeStructKind[F, A]): Int = kind.alignment
}
