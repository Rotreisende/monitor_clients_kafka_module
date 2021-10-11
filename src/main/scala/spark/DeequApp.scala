package spark

import CheckBuilder._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame

object DeequApp extends App {
  val spark =
    SparkSession.builder()
      .master("local")
      .appName("suggestion")
      .getOrCreate()

  def readCsv(path: String)(implicit spark: SparkSession): DataFrame = {
    spark.read
      .option("header", value = true)
      .option("sep", ",")
      .option("quote", "\"")
      .csv(path)
  }

  val errorsDf = readCsv("kafka-playground/src/main/resources/errors.csv")(spark)
  val dataDf = readCsv("kafka-playground/src/main/resources/data.csv")(spark)

  val result = dataDf.getCheckResult(errorsDf)(spark)

  //dataDf.foreach(row => println(Instrument(errorsDf).validate(row)))// not work
  //println(Instrument(errorsDf).validate(dataDf.collect().head))// work
}