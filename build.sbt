import DependencySettings._

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary)

val settingsHelper = ProjectSettingsHelper("au.id.tmm","ausgeo")()

settingsHelper.settingsForBuild

lazy val root = project
  .in(file("."))
  .settings(settingsHelper.settingsForRootProject)
  .settings(console := (console in Compile in core).value)
  .aggregate(
    core,
    codecs,
  )

lazy val core = project
  .in(file("core"))
  .settings(settingsHelper.settingsForSubprojectCalled("core"))

lazy val codecs = project
  .in(file("codecs"))
  .settings(settingsHelper.settingsForSubprojectCalled("codecs"))
  .settings(
    circeDependency,
  )
  .dependsOn(core)

addCommandAlias("check", ";+test;scalafmtCheckAll")
