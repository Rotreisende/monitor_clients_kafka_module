package spark

import com.amazon.deequ.checks.{Check, CheckWithLastConstraintFilterable}
import com.amazon.deequ.{VerificationResult, VerificationRunBuilder, VerificationSuite}
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession}

object CheckBuilder {
  def getVerificationBuilder(dataFrame: DataFrame): VerificationRunBuilder = {
    VerificationSuite()
      .onData(dataFrame)
  }

  def getVerificationResult(dataFrame: DataFrame)(checks: Seq[Check]): VerificationResult = {
    getVerificationBuilder(dataFrame)
      .addChecks(checks)
      .run()
  }

  def getCheckResult(dataFrame: DataFrame, errorDf: DataFrame)(implicit sparkSession: SparkSession): DataFrame = {
    VerificationResult.checkResultsAsDataFrame(sparkSession, getVerificationResult(dataFrame)(getChecks(errorDf)))
  }

  def getChecks(df: DataFrame): Array[CheckWithLastConstraintFilterable] = {
    implicit val encoder: Encoder[CheckWithLastConstraintFilterable] = Encoders.kryo[CheckWithLastConstraintFilterable]
    df.map(CheckInstrument().mapFunction).collect()
  }
}
