import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{ByteArraySerializer, LongSerializer}
import ru.releaseSbtTest.user.User
import util.AvroSerializer

import java.util.{Properties, UUID}

object Producer {
  def prepareProducerProps(): Properties = {
    val props = new Properties
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-playground")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[ByteArraySerializer].getName)
    props
  }

  def createProducer(): KafkaProducer[Long, Array[Byte]] = {
    new KafkaProducer[Long, Array[Byte]](prepareProducerProps())
  }

  def main(args: Array[String]): Unit = {
    val producer = createProducer()
    try {
      val user = User(UUID.randomUUID().toString, "Ivanov", "Ivan", None, 18)
      producer.send(new ProducerRecord[Long, Array[Byte]]("topic_1", 2, AvroSerializer.toByteArray(user)))
      println("Send message")
    } catch {
      case e: Throwable => println(e.getCause)
    }
  }
}
