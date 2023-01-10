package dev.axyria.scalastruct

import cats.effect.*

import java.lang.reflect.Modifier

/** A reference to a native memory address pointer using opaque types to have
  * the less overhead possible. This is a wrapper around common pointer
  * operations and is intended to be used in conjunction with [[RawType]].
  *
  * @author
  *   Pedro H.
  */
type Pointer = Pointer.Type

/** A reference to a native memory address pointer using opaque types to have
  * the less overhead possible. This is a wrapper around common pointer
  * operations and is intended to be used in conjunction with [[RawType]].
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

  extension (pointer: Type)
    // general-purpose methods
    def toLong: Long          = pointer
    def +(offset: Long): Type = pointer + offset
    def -(offset: Long): Type = pointer - offset
    def *(offset: Long): Type = pointer * offset
    def /(offset: Long): Type = pointer / offset
