package spark

import com.amazon.deequ.checks.{Check, CheckLevel, CheckWithLastConstraintFilterable}
import org.apache.spark.sql.Row

import scala.util.matching.Regex

case class CheckInstrument() extends Serializable {
  def mapFunction(row: Row): CheckWithLastConstraintFilterable = {
    val pattern = row.getString(2)
    pattern match {
      case "isEmpty" => getCheck(s"${row.getString(1)}: ${row.getString(3)}", row.getString(0))
      case _ => getCheck(s"${row.getString(1)}: ${row.getString(3)}", row.getString(0), pattern.r)
    }
  }

  def getCheck(description: String, header: String, pattern: Regex = null): CheckWithLastConstraintFilterable = {
    val check = createCheckBody(description)
    if (pattern == null) {
      addComplete(check, header)
    } else {
      addPattern(check, header, pattern)
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
