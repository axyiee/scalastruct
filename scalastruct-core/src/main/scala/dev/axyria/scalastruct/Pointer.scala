package dev.axyria.scalastruct

/** A reference to a native memory address pointer using opaque types to have the less overhead possible. This
  * is a wrapper around common pointer operations and is intended to be used in conjunction with [[RawType]].
  *
  * @author
  *   Pedro H.
  */
type Pointer = Pointer.Type

/** A reference to a native memory address pointer using opaque types to have the less overhead possible. This
  * is a wrapper around common pointer operations and is intended to be used in conjunction with [[RawType]].
  *
  * @author
  *   Pedro H.
  */
object Pointer:
  opaque type Type = Long

  /** Creates a pointer from a long value.
    *
    * @param value
    *   the long value.
    * @return
    *   the pointer.
    */
  def apply(value: Long): Type =
    value

  /** Calculates the address of an aligned pointer.
    * @param base
    *   The base address.
    * @param index
    *   The index if it is a multibyte element.
    * @param alignment
    *   The alignment of the structure kind.
    * @return
    *   The aligned address.
    */
  inline def buildAddress(base: Pointer, index: Long, alignment: Int): Pointer =
    Pointer(base.toLong + index * alignment)

  extension (pointer: Type)
    // general-purpose methods
    inline def toLong: Long                    = pointer
    inline def +(offset: Long | Pointer): Type = pointer + offset
    inline def -(offset: Long | Pointer): Type = pointer - offset
    inline def *(offset: Long | Pointer): Type = pointer * offset
    inline def /(offset: Long | Pointer): Type = pointer / offset
