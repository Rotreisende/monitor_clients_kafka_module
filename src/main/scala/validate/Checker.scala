package validate

import scala.util.matching.Regex

object Checker {
  def checkNative(pattern: Regex, value: Option[String]): Boolean = {
    value match {
      case Some(item) => isValidPattern(pattern, item)
      case None => false
    }
  }

  def checkOption(pattern: Regex, value: Option[String]): Boolean = {
    value match {
      case None => true
      case Some(_) => checkNative(pattern, value)
    }
  }

  def isValidPattern(pattern: Regex, value: String): Boolean = {
    pattern.findFirstMatchIn(value) match {
      case Some(_) => true
      case None => false
    }
  }
}