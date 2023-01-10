package dev.axyria.scalastruct

/** A low-level and unsafe type class regarding explicit native (heap) memory management within the JVM. It is
  * also useful to use [[sun.misc.Unsafe]] as a behind-the-scenes implementation whenever possible to ensure
  * maximum performance and efficiency when working with native memory.
  *
  * @tparam F
  *   The monoid where the computations will be performed.
  * @tparam A
  *   The kind of structure to be dynamically managed.
  * @author
  *   Pedro H.
  */
trait UnsafeStructKind[F[_], A]:
  /** The memory alignment for this type. An alignment is the number of bytes a value that a memory pointer
    * points to must be divisible by in order to be valid for this type.
    */
  def alignment: Int

  /** Allocates a value of type [A] to the memory and returns its result address.
    *
    * @param data
    *   The data to allocate to the heap.
    *
    * @return
    *   A computation of a memory allocation process.
    */
  def alloc(data: A): F[Pointer]

  /** Allocates an empty structure within the given [length] directly into the memory table.
    *
    * @param length
    *   The length of the structure to be allocated.
    *
    * @return
    *   A computation of a memory allocating process + the start of the result memory block.
    */
  def allocEmpty(length: Long): F[Pointer]

  /** Reallocates a value of type [A] at the given [address]. This will modify the value at the address given
    * by the formula `address + index * alignment`.
    *
    * @param address
    *   The address to reallocate at.
    * @param index
    *   The index to reallocate at. It can of course be set to zero if it isn't necessary.
    * @param data
    *   The new data to be allocated.
    * @return
    *   A computation of a memory reallocating process then returns its address.
    */
  def store(address: Pointer, index: Long, data: A): F[Pointer]

  /** Deserialize the given [[address]] data back into data of type [A].
    */
  def read(address: Pointer): F[A]

  /** Return whether the given pointer is valid for this type.
    *
    * @param pointer
    *   The pointer to check.
    * @return
    *   Whether the pointer is valid for this type.
    */
  def is(pointer: Pointer): Boolean =
    pointer.toLong % alignment == 0

  extension (pointer: Pointer)
    /** Deserialize this [[Pointer]] data back into data of type [A].
      */
    def readAs: F[A] = this.read(pointer)
