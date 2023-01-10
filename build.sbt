ThisBuild / tlBaseVersion := "0.1"

ThisBuild / organization     := "dev.axyria"
ThisBuild / organizationName := "aaxyi"
ThisBuild / startYear        := Some(2023)
ThisBuild / licenses         := Seq(License.MIT)
ThisBuild / developers       := List(tlGitHubDev("aaxyi", "Pedro Henrique"))

// publish to s01.oss.sonatype.org (set to true to publish to oss.sonatype.org instead)
ThisBuild / tlSonatypeUseLegacyHost := false

ThisBuild / scalaVersion                                   := "3.2.1"
ThisBuild / semanticdbEnabled                              := true
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val deps = new {
  val typelevel = Seq(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-effect" % "3.4.4",
      "org.typelevel" %%% "cats-core"   % "2.9.0",
    )
  )
  val test = Seq(
    libraryDependencies ++= Seq(
      "org.scalameta" %%% "munit"               % "0.7.29" % Test,
      "org.typelevel" %%% "munit-cats-effect-3" % "1.0.7"  % Test
    )
  )
}

lazy val core = crossProject(JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("scalastruct-core"))
  .settings(name := "scalastruct-core", semanticdbEnabled := true, deps.typelevel, deps.test)

lazy val benchmarks = project
  .in(file("benchmarks"))
  .dependsOn(core.jvm)
  .settings(name := "scalastruct-benchmarks", deps.typelevel)
  .enablePlugins(JmhPlugin)

lazy val root = tlCrossRootProject.aggregate(core)
