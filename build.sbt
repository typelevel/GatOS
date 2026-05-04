import laika.sbt.LaikaConfig
import laika.config.*

ThisBuild / tlBaseVersion := "0.0"
ThisBuild / startYear     := Some(2024)
ThisBuild / licenses      := Seq(License.Apache2)

ThisBuild / developers := List(
  tlGitHubDev("ChristopherDavenport", "Christopher Davenport"),
  tlGitHubDev("TonioGela", "Antonio Gelameris"),
  tlGitHubDev("Hombre-x", "Gabriel Santana Paredes")
)

ThisBuild / tlSitePublishBranch        := Some("main")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))

ThisBuild / crossScalaVersions := Seq("2.13.18", "3.3.7")

lazy val root = tlCrossRootProject.aggregate(gatos, examples)

lazy val gatos = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("gatos"))
  .settings(
    name := "gatos",
    libraryDependencies ++= List(
      "org.typelevel" %% "cats-core"   % "2.13.0",
      "org.typelevel" %% "cats-effect" % "3.7.0",
      "co.fs2"        %% "fs2-core"    % "3.13.0",
      "co.fs2"        %% "fs2-io"      % "3.13.0",
      "org.scodec"    %% "scodec-bits" % "1.2.4",
      "org.scodec" %% "scodec-core" % (if (scalaVersion.value.startsWith("2."))
                                         "1.11.10"
                                       else "2.3.3"),
      // Testing
      "org.typelevel" %% "weaver-cats"       % "0.12.0" % Test,
      "org.typelevel" %% "weaver-scalacheck" % "0.12.0" % Test
    )
  )

lazy val examples = project
  .in(file("examples"))
  .enablePlugins(NoPublishPlugin)
  .dependsOn(gatos.jvm)
  .settings(
    name                 := "gatos-examples",
    Compile / run / fork := true
  )

lazy val docs = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .dependsOn(gatos.jvm)
  .settings(
    libraryDependencies += "co.fs2" %% "fs2-scodec" % "3.13.0",
    laikaConfig := LaikaConfig.defaults
      .withConfigValue(
        Selections(
          SelectionConfig(
            "api-style",
            ChoiceConfig("syntax", "Syntax"),
            ChoiceConfig("static", "Static"),
            ChoiceConfig("fs2", "Fs2")
          )
        )
      )
  )
