import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, LongDeserializer}
//import ru.releaseSbtTest.user.User
import util.AvroDeserializer

import java.time.Duration
import java.util.Properties
import scala.collection.JavaConverters._

object Consumer {
  def prepareConsumerProps(): Properties = {
    val props = new Properties
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "TEST_GROUP")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[LongDeserializer])
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[ByteArrayDeserializer])
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
    props
  }

  def main(args: Array[String]): Unit = {
    val kafkaConsumer = new KafkaConsumer[Long, Array[Byte]](prepareConsumerProps())
    kafkaConsumer.subscribe(Seq("topic_1").asJava)
    kafkaConsumer.poll(Duration.ofMillis(0))

    while (true) {
      try {
        val results = kafkaConsumer.poll(Duration.ofMillis(2000)).asScala
        results.foreach { record =>
          //println(s"key: ${record.key()}, value: ${AvroDeserializer.fromByteArray(User.SCHEMA$, record.value())}")
        }
      } catch {
        case e: Throwable => println(e.getCause)
      }
    }
  }
}
