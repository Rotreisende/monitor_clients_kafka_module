package spark

import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.functions.col

import scala.util.matching.Regex

object PatternInstrument {
  implicit class PatternsDataFrame(errorDf: DataFrame) {
    def getPatterns: Dataset[Row] = {
      errorDf
        .drop("codeError")
        .drop("description")
        .filter(errorDf("predicate") =!= "isEmpty")
    }

    def getPattern(value: String): Regex = {
      val ds = getPatterns
      ds
        .filter(col("field") === value)
        .collect()
        .head
        .getString(1)
        .r
    }
  }
}
