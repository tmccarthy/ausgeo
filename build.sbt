name := "ausgeo"

ThisBuild / tlBaseVersion := "0.3"

Sonatype.SonatypeKeys.sonatypeProfileName := "au.id.tmm"
ThisBuild / organization := "au.id.tmm.ausgeo"
ThisBuild / organizationName := "Timothy McCarthy"
ThisBuild / startYear := Some(2022)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  tlGitHubDev("tmccarthy", "Timothy McCarthy"),
)

val Scala213 = "2.13.8"
ThisBuild / scalaVersion := Scala213
ThisBuild / crossScalaVersions := Seq(
  Scala213,
//  "3.1.3", // TODO
)

ThisBuild / githubWorkflowJavaVersions := List(
  JavaSpec.temurin("11"),
  JavaSpec.temurin("17"),
)

ThisBuild / tlCiHeaderCheck := false
ThisBuild / tlCiScalafmtCheck := true
ThisBuild / tlCiMimaBinaryIssueCheck := false
ThisBuild / tlFatalWarnings := true

addCommandAlias("check", ";githubWorkflowCheck;scalafmtSbtCheck;+scalafmtCheckAll;+test")
addCommandAlias("fix", ";githubWorkflowGenerate;+scalafmtSbt;+scalafmtAll")

val scalatestVersion = "3.0.8" // TODO move to mUnit

lazy val root = tlCrossRootProject
  .aggregate(
    core,
    circe,
  )

lazy val core = project
  .in(file("core"))
  .settings(name := "ausgeo-core")
  .settings(
    libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % Test,
  )

lazy val circe = project // TODO should rename to "circe"
  .in(file("circe"))
  .settings(name := "ausgeo-circe")
  .dependsOn(core)
  .settings(
    libraryDependencies += "io.circe" %% "circe-core" % "0.15.0-M1",
  )
  .settings(
    libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % Test,
  )
