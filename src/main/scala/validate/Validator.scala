package validate

import Headers._
import Checker._
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import spark.PatternInstrument._

case object Validator extends Serializable {
  implicit class RichDataFrameValidator(errorDf: DataFrame) extends Serializable {
    def getValidateRows(df: DataFrame): Dataset[Row] = {
      df.filter(row => Instrument(errorDf).validate(row))
    }
  }

  case class Instrument(errorDf: DataFrame) extends Serializable {
    def validate(row: Row): Boolean = {
      checks.forall { header =>
        val index = getIndexField(header, row)
        isValid(header, Option(row.getString(index)))
      }
    }

    def getIndexField(field: Header, row: Row): Int = {
      row.fieldIndex(field.convertToString())
    }

    def isValid(header: Header, value: Option[String]): Boolean = {
      if (options.contains(header)) {
        checkOption(errorDf.getPattern(header.convertToString()), value)
      } else {
        checkNative(errorDf.getPattern(header.convertToString()), value)
      }
    }
  }
}
