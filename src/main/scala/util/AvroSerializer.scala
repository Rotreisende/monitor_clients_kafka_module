package util

import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.{SpecificDatumWriter, SpecificRecordBase}
import util.FileIO.withByteOutStream

object AvroSerializer {
  def toByteArray[T <: SpecificRecordBase](record: T): Array[Byte] = {
    withByteOutStream { byteStream =>
      val binaryEncoder = EncoderFactory.get().binaryEncoder(byteStream, null)
      val writer = new SpecificDatumWriter[T](record.getSchema)
      writer.write(record, binaryEncoder)
      binaryEncoder.flush()
      byteStream.toByteArray
    }
  }
}
