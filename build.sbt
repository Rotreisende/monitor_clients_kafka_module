lazy val root = (project in file("."))
  .settings(
    name:= "ReleaseSbtTest",
    version := "0.1",
    crossScalaVersions := Nil,
    scalaVersion := "2.13.6"
  )
  .aggregate(avro, kafka, flink)

lazy val avro =
  Project(id = "avro-playground", base = file("avro-playground"))
    .enablePlugins(SbtAvrohugger)
    .settings(
      (Compile / sourceGenerators) += (Compile / avroScalaGenerateSpecific).taskValue,
      libraryDependencies ++= Dependencies.avro
    )

lazy val kafka =
  Project(id = "kafka-playground", base = file("kafka-playground"))
    .settings(
      libraryDependencies ++= Dependencies.avro ++ Dependencies.kafka ++ Dependencies.spark ++ Dependencies.deequ ++ Dependencies.jackson
    )
    .dependsOn(avro)

lazy val flink =
  Project(id = "flink-playground", base = file("flink-playground"))
    .settings(
      libraryDependencies ++= Dependencies.flink ++ Dependencies.flinkConnectorToKafka
    )
    .dependsOn(avro)