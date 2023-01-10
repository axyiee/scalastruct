package dev.axyria.scalastruct

/** A type regarding all possible primitives for an unsafe operation.
  */
type Primitive = Byte | Int | Long | Char | Double | Float | Short

/** A factory helper responsible for managing the allocation and freeing of new pointers.
  *
  * @tparam F
  *   The monoid where the computations will be performed.
  * @author
  *   Pedro H.
  */
trait UnsafePointerFactory[F[_]]:
  /** Computes a new memory allocation under the given [size].
    *
    * @param size
    *   The size of the allocation.
    * @return
    *   The computation of a new memory allocation.
    */
  def alloc(size: Long): F[Pointer]

  /** Reallocates a value at a specific memory [[address]] with the given [[data]].
    *
    * @param address
    *   The memory address to be reallocated.
    * @param data
    *   The data to be reallocated at the given memory [[address]].
    * @return
    *   The computation of a memory reallocation.
    */
  def realloc[A <: Primitive](address: Pointer, data: A)(using PrimitiveMemoryFactory[F, A]): F[Unit]

  /** Computes the freeing of an existing memory allocation under the given [address].
    *
    * @param address
    *   The address of the allocation.
    * @return
    *   The computation of a memory freeing process.
    */
  def freePointer(address: Pointer): F[Unit]

  /** Access the [[Primitive]] value with the given [[offset]] of the given [[address]].
    *
    * @param address
    *   The address to be accessed.
    * @return
    *   The computation of that memory accessing.
    */
  def visit[A <: Primitive](address: Pointer)(using PrimitiveMemoryFactory[F, A]): F[A]

  extension (@annotation.unused pointer: Pointer.type)
    /** Allocates a structure into the native memory (heap). This returns the address of the start of the
      * allocated memory. This is really useful when working with large structure like arrays.
      *
      * @param size
      *   The size of the structure to allocate to the memory.
      * @return
      *   A computation of a memory allocation.
      */
    def allocate(size: Int): F[Pointer] = this.alloc(size)

  extension (pointer: Pointer)

    /** Frees the memory taken by the memory referenced by this pointer.
      */
    def free(): F[Unit] =
      this.freePointer(pointer)

    /** Access the [[Byte]] value with the given [[offset]] of this address.
      */
    def access[A <: Primitive]()(using PrimitiveMemoryFactory[F, A]): F[A] = this.visit(pointer)

    /** Reallocates a value at this memory address with the given [[data]].
      *
      * @param data
      *   The data to be reallocated at the given memory [[address]].
      * @return
      *   The computation of a memory reallocation.
      */
    def store[A <: Primitive](data: A)(using PrimitiveMemoryFactory[F, A]): F[Unit] =
      this.realloc(pointer, data)

/** Acts into writing or reading values of/into the memory table for a specific primitive kind.
  *
  * @tparam F
  *   The monoid where the computations will be performed.
  * @tparam A
  *   The primitive kind to be managed.
  */
trait PrimitiveMemoryFactory[F[_], A <: Primitive]:
  def write(address: Pointer, data: A): F[Unit]
  def read(address: Pointer): F[A]
