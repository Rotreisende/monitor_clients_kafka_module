package spark

import CheckBuilder._
import org.apache.spark.sql.SparkSession

object DeequApp extends App {
  def getSparkSession() = {
    SparkSession.builder()
      .master("local")
      .appName("suggestion")
      .getOrCreate()
  }

  def readCsv(path: String)(implicit spark: SparkSession) = {
    spark.read
      .option("header", value = true)
      .option("sep", ",")
      .option("quote", "\"")
      .csv(path)
  }

  implicit val sparkSession: SparkSession = getSparkSession()

  val errorsDf = readCsv("kafka-playground/src/main/resources/errors.csv")
  val dataDf = readCsv("kafka-playground/src/main/resources/data.csv")

  val result = getCheckResult(dataDf, errorsDf)

  errorsDf.show()
  dataDf.show()
  result.show()
}
