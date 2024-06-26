import Dependencies._

val Scala2Version = "2.13.14"
val Scala3Version = "3.4.1"

ThisBuild / scalaVersion := Scala3Version // For JDK 16 compatibility

ThisBuild / organization := "com.gregorpurdy"
ThisBuild / version := "0.3.1-SNAPSHOT"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / organizationName := "Gregor Purdy"
ThisBuild / organizationHomepage := Some(url("https://github.com/gnp"))
ThisBuild / description := "Scala classes for representing syntactically valid identifiers of various kinds."
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/gnp/ident"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/gnp/ident"),
    "scm:git@github.com:gnp/ident.git"
  )
)
ThisBuild / developers := List(
  Developer(
    "gnp",
    "Gregor Purdy",
    "gregor@abcelo.com",
    url("http://github.com/gnp")
  )
)

// Remove all additional repository other than Maven Central from POM
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := sonatypePublishToBundle.value

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

addCommandAlias(
  "check",
  "; headerCheck; scalafmtSbtCheck; scalafmtCheckAll; scalafixAll --check; doc"
)

addCommandAlias(
  "generateReadme",
  "; project docs; set mdocIn := file(\"docs/index.md\"); set mdocOut := file(\"README.md\"); doc / mdoc"
)

val stdCompilerOptions2 = Seq(
  "-Ytasty-reader",
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Wdead-code", // Warn when dead code is identified.
  "-Wextra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Wmacros:before", // Enable lint warnings on macro expansions. Default: `before`, `help` to list choices.
  "-Wnumeric-widen", // Warn when numerics are widened.
  "-Woctal-literal", // Warn on obsolete octal syntax.
  "-Wunused:explicits", // Warn if an explicit parameter is unused.
//  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn if an import selector is not referenced.
  "-Wunused:linted", // -Xlint:unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:params", // Enable -Wunused:explicits,implicits.
  "-Wunused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates", // Warn if a private member is unused.
  "-Wvalue-discard", // Warn when non-Unit expression results are unused.
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:deprecation", // Enable linted deprecations.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:eta-sam", // Warn on eta-expansion to meet a Java-defined functional interface that is not explicitly annotated with @FunctionalInterface.
  "-Xlint:eta-zero", // Warn on eta-expansion (rather than auto-application) of zero-ary method.
  "-Xlint:implicit-not-found", // Check @implicitNotFound and @implicitAmbiguous messages.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:-infer-any", // DISABLED (because ZIO) Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nonlocal-return", // A return statement used an exception for flow control.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:serial", // @SerialVersionUID on traits and non-serializable classes.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Xlint:unused", // Enable -Ywarn-unused:imports,privates,locals,implicits.
  "-Xlint:valpattern", // Enable pattern checks in val definitions.
  "-Xsource:3",
  "-Xmigration",
  "-Wconf:msg=method are copied from the case class constructor:s",
  "-Xfatal-warnings"
)

val stdCompilerOptions3 = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Wunused:explicits", // Warn if an explicit parameter is unused.
//  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn if an import selector is not referenced.
  "-Wunused:linted", // -Xlint:unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:params", // Enable -Wunused:explicits,implicits.
  "-Wunused:privates", // Warn if a private member is unused.
  "-Wvalue-discard", // Warn when non-Unit expression results are unused.
  "-Xfatal-warnings"
)

lazy val root = (project in file("."))
  .aggregate(
    ident,
    identCirce,
    identZioConfig,
    identZioJson,
    identZioSchema,
    bench,
    examples
  )
  .settings(
    name := "root",
    crossScalaVersions := Nil, // To avoid "double publishing"
    publish := {},
    publish / skip := true,
    publishLocal := {}
  )

lazy val docs = project
  .in(file("ident-docs"))
  .settings(
    mdocVariables := Map(
      "VERSION" -> version.value.replaceAll("-SNAPSHOT$", "")
    )
  )
  .dependsOn(ident)
  .enablePlugins(MdocPlugin)

lazy val examples = (project in file("examples"))
  .dependsOn(ident, identCirce, identZioConfig, identZioJson, identZioSchema)
  .settings(
    name := "examples",
    crossScalaVersions := Nil,
    publish := {},
    publish / skip := true,
    publishLocal := {},
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      CirceCore % Compile,
      CirceGeneric % Compile,
      CirceParser % Compile,
      ZioSchemaJson % Compile
    )
  )

lazy val bench = (project in file("bench"))
  .dependsOn(ident)
  .enablePlugins(JmhPlugin)
  .settings(
    name := "bench",
    crossScalaVersions := Nil,
    publish := {},
    publish / skip := true,
    publishLocal := {},
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      ZioTest % Compile
    )
  )

lazy val ident = (project in file("ident"))
  .settings(
    name := "ident",
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      Slf4JApi % Test,
      JclOverSlf4J % Test,
      Log4JOverSlf4J % Test,
      JulToSlf4J % Test,
      Logback % Test,
      ZioTest % Test,
      ZioTestMagnolia % Test,
      ZioTestSbt % Test
    )
      .map(_.exclude("commons-logging", "commons-logging"))
      .map(_.exclude("log4j", "log4j"))
      .map(_.exclude("org.slf4j", "slf4j-log4j12")),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val identCirce = (project in file("ident-circe"))
  .dependsOn(ident)
  .settings(
    name := "ident-circe",
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      CirceCore % Compile,
      Slf4JApi % Test,
      JclOverSlf4J % Test,
      Log4JOverSlf4J % Test,
      JulToSlf4J % Test,
      CirceParser % Test,
      Logback % Test,
      ZioTest % Test,
      ZioTestMagnolia % Test,
      ZioTestSbt % Test
    )
      .map(_.exclude("commons-logging", "commons-logging"))
      .map(_.exclude("log4j", "log4j"))
      .map(_.exclude("org.slf4j", "slf4j-log4j12")),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val identZioConfig = (project in file("ident-zio-config"))
  .dependsOn(ident)
  .settings(
    name := "ident-zio-config",
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      ZioConfig % Compile,
      ZioConfigMagnolia % Compile,
      Slf4JApi % Test,
      JclOverSlf4J % Test,
      Log4JOverSlf4J % Test,
      JulToSlf4J % Test,
      Logback % Test,
      ZioTest % Test,
      ZioTestMagnolia % Test,
      ZioTestSbt % Test
    )
      .map(_.exclude("commons-logging", "commons-logging"))
      .map(_.exclude("log4j", "log4j"))
      .map(_.exclude("org.slf4j", "slf4j-log4j12")),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val identZioJson = (project in file("ident-zio-json"))
  .dependsOn(ident)
  .settings(
    name := "ident-zio-json",
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      ZioJson % Compile,
      Slf4JApi % Test,
      JclOverSlf4J % Test,
      Log4JOverSlf4J % Test,
      JulToSlf4J % Test,
      Logback % Test,
      ZioTest % Test,
      ZioTestMagnolia % Test,
      ZioTestSbt % Test
    )
      .map(_.exclude("commons-logging", "commons-logging"))
      .map(_.exclude("log4j", "log4j"))
      .map(_.exclude("org.slf4j", "slf4j-log4j12")),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val identZioSchema = (project in file("ident-zio-schema"))
  .dependsOn(ident)
  .settings(
    name := "ident-zio-schema",
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      ZioSchema % Compile,
      ZioSchemaDerivation % Compile,
      ZioSchemaJson % Test,
      Slf4JApi % Test,
      JclOverSlf4J % Test,
      Log4JOverSlf4J % Test,
      JulToSlf4J % Test,
      Logback % Test,
      ZioTest % Test,
      ZioTestMagnolia % Test,
      ZioTestSbt % Test
    )
      .map(_.exclude("commons-logging", "commons-logging"))
      .map(_.exclude("log4j", "log4j"))
      .map(_.exclude("org.slf4j", "slf4j-log4j12")),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
