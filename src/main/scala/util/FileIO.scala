package util

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

object FileIO {
  def withByteInputStream[T](bytes: Array[Byte])(body: ByteArrayInputStream => T): T = {
    val byteStream = new ByteArrayInputStream(bytes)
    try {
      body(byteStream)
    } finally {
      byteStream.close()
    }
  }

  def withByteOutStream[T](body: ByteArrayOutputStream => T): T = {
    val byteStream = new ByteArrayOutputStream()
    try {
      body(byteStream)
    } finally {
      byteStream.close()
    }
  }
}
