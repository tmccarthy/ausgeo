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
  "3.1.3",
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

val mUnitVersion           = "0.7.27"
val disciplineMUnitVersion = "1.0.9"
val circeVersion           = "0.15.0-M1"
val catsVersion            = "2.8.0"

lazy val root = tlCrossRootProject
  .aggregate(
    core,
    circe,
    cats,
    scalacheck,
  )

lazy val core = project
  .in(file("core"))
  .settings(name := "ausgeo-core")
  .settings(
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies += "org.scalameta" %% "munit" % mUnitVersion % Test,
  )

lazy val scalacheck = project
  .in(file("scalacheck"))
  .settings(name := "ausgeo-scalacheck")
  .dependsOn(core)
  .settings(
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.16.0",
    libraryDependencies += "org.typelevel"  %% "cats-laws"  % catsVersion,
  )
  .settings(
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies += "org.scalameta" %% "munit"            % mUnitVersion           % Test,
    libraryDependencies += "org.typelevel" %% "discipline-munit" % disciplineMUnitVersion % Test,
  )

lazy val cats = project
  .in(file("cats"))
  .settings(name := "ausgeo-cats")
  .dependsOn(core, scalacheck % "compile->test")
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-kernel" % catsVersion,
  )
  .settings(
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies += "org.scalameta" %% "munit"            % mUnitVersion           % Test,
    libraryDependencies += "org.typelevel" %% "discipline-munit" % disciplineMUnitVersion % Test,
  )

lazy val circe = project
  .in(file("circe"))
  .settings(name := "ausgeo-circe")
  .dependsOn(core, cats, scalacheck % "compile->test")
  .settings(
    libraryDependencies += "io.circe" %% "circe-core" % circeVersion,
  )
  .settings(
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies += "org.scalameta" %% "munit"            % mUnitVersion           % Test,
    libraryDependencies += "io.circe"      %% "circe-testing"    % circeVersion           % Test,
    libraryDependencies += "org.typelevel" %% "discipline-munit" % disciplineMUnitVersion % Test,
  )
