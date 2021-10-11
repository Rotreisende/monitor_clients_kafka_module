package spark

import com.amazon.deequ.checks.{Check, CheckWithLastConstraintFilterable}
import com.amazon.deequ.{VerificationResult, VerificationRunBuilder, VerificationSuite}
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession}

object CheckBuilder {
  implicit class RichDataFrame(df: DataFrame) {
    def getVerificationBuilder: VerificationRunBuilder = {
      VerificationSuite()
        .onData(df)
    }

    def getVerificationResult(checks: Seq[Check]): VerificationResult = {
      getVerificationBuilder
        .addChecks(checks)
        .run()
    }

    def getCheckResult(errorDf: DataFrame)(implicit sparkSession: SparkSession): DataFrame = {
      VerificationResult.checkResultsAsDataFrame(sparkSession, getVerificationResult(getChecks(errorDf)))
    }

    def getChecks(errorDf: DataFrame): Array[CheckWithLastConstraintFilterable] = {
      implicit val encoder: Encoder[CheckWithLastConstraintFilterable] = Encoders.kryo[CheckWithLastConstraintFilterable]
      errorDf.map(CheckInstrument.mapFunction).collect()
    }
  }


}