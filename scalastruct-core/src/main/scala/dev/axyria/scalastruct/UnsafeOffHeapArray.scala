package dev.axyria.scalastruct

import dev.axyria.scalastruct.UnsafeOffHeapArray.offset

/** A simple implementation of a fixed-length off-heap - using native memory not managed by the garbage
  * collector - array.
  *
  * @author
  *   Pedro H.
  * @param length
  *   The length/capacity of this array.
  * @tparam A
  *   The type of the elements of the array.
  * @author
  *   Pedro H.
  */
case class UnsafeOffHeapArray[A](address: Pointer, capacity: Long)(using kind: UnsafeStructKind[A]) {

  /** Modifies the memory stack to add an element to this array.
    *
    * @param index
    *   The index of the element to be added or replaced.
    * @param value
    *   The value matching the given index.
    * @return
    *   A computation of a memory reallocating.
    */
  inline def set(index: Long, value: A): Pointer =
    if index < 0 || index >= capacity then
      throw IndexOutOfBoundsException(s"Index $index is out of bounds for array of size $capacity.")
    kind.store(address, index, value)

  /** Retrieves a value from this off-heap array directly from the memory stack.
    *
    * @param index
    *   The index of the element to be retrieved.
    * @return
    *   A computation of memory value retrieving then converting it to [A].
    */
  inline def get(index: Long): A =
    if index < 0 || index >= capacity then
      throw IndexOutOfBoundsException(s"Index $index is out of bounds for array of size $capacity.")
    kind.read(Pointer.buildAddress(address, index, offset[A]))

  /** Frees/invalidates this array.
    *
    * @return
    *   A computation of a memory deallocation process.
    */
  inline def free(): Unit =
    address.free()

  /** Reallocates this array to a new capacity and returns an updated array.
    *
    * @param newCapacity
    *   The new capacity of this array.
    * @return
    *   A new array with the same [[address]] but reallocated with a new [[capacity]].
    */
  inline def withCapacity(newCapacity: Long): UnsafeOffHeapArray[A] =
    kind.realloc(address, newCapacity)
    UnsafeOffHeapArray(address, newCapacity)
}

object UnsafeOffHeapArray {

  /** Initializes this array by allocating the size of all elements expected to be in this array.
    *
    * @param capacity
    *   The capacity of this array.
    * @return
    *   A computation of a full array memory allocation followed by a new helper class for managing unsafe off
    *   heap arrays with ease.
    */
  def apply[A](capacity: Long)(using kind: UnsafeStructKind[A]): UnsafeOffHeapArray[A] =
    if capacity <= 0 then
      throw IllegalArgumentException("The capacity of the unsafe off-heap array must be greater than 0.")
    UnsafeOffHeapArray(kind.allocEmpty(capacity), capacity)

  /** The offset between the start of each element. This expects all elements to be primitives or simply have
    * a fixed size (non-dynamic) size between all elements.
    */
  inline def offset[A](using kind: UnsafeStructKind[A]): Int = kind.alignment
}
