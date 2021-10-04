package util

import org.apache.avro.Schema
import org.apache.avro.io.DecoderFactory
import org.apache.avro.specific.{SpecificDatumReader, SpecificRecordBase}
import util.FileIO.withByteInputStream

object AvroDeserializer {
  def fromSchemaByteArray[T <: SpecificRecordBase](schemaContainer: T, bytes: Array[Byte]): T = {
    fromByteArray(schemaContainer.getSchema, bytes)
  }

  def fromByteArray[T <: SpecificRecordBase](schema: Schema, bytes: Array[Byte]): T = {
    val reader = new SpecificDatumReader[T](schema)
    withByteInputStream(bytes) { byteStream =>
      val binaryDecoder = DecoderFactory.get().binaryDecoder(byteStream, null)
      reader.read(null.asInstanceOf[T], binaryDecoder)
    }
  }
}
