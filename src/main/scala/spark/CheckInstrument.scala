package spark

import com.amazon.deequ.checks.{Check, CheckLevel, CheckWithLastConstraintFilterable}
import org.apache.spark.sql.{DataFrame, Dataset, Row}

import scala.util.matching.Regex

case object CheckInstrument extends Serializable {
  val isEmptyValuePattern = "isEmpty"

  def mapFunction(row: Row): CheckWithLastConstraintFilterable = {
    val pattern = row.getString(2)
    pattern match {
      case value if value == isEmptyValuePattern => getCheck(s"${row.getString(1)}: ${row.getString(3)}", row.getString(0))
      case _ => getCheck(s"${row.getString(1)}: ${row.getString(3)}", row.getString(0), Option(pattern.r))
    }
  }

  def getCheck(description: String, header: String, pattern: Option[Regex] = None): CheckWithLastConstraintFilterable = {
    val check = createCheckBody(description)
    pattern match {
      case Some(pattern) => addPattern(check, header, pattern)
      case None => addComplete(check, header)
    }
  }

  def createCheckBody(description: String): Check = {
    Check(CheckLevel.Error, description)
  }

  def addComplete(check: Check, header: String): CheckWithLastConstraintFilterable = {
    check.isComplete(header)
  }

  def addPattern(check: Check, header: String, pattern: Regex): CheckWithLastConstraintFilterable = {
    check.hasPattern(header, pattern)
  }
}