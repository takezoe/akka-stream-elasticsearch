name := "akka-stream-elasticsearch"

organization := "com.github.takezoe"

version := "1.0.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-stream"                  % "2.5.1",
  "org.elasticsearch.client"   %  "rest"                         % "5.2.2",
  "io.spray"                   %% "spray-json"                   % "1.3.3",
  "com.fasterxml.jackson.core" %  "jackson-databind"             % "2.9.0.pr3",
  "com.typesafe.akka"          %% "akka-stream-testkit"          % "2.5.1"     % "test",
  "org.codelibs"               %  "elasticsearch-cluster-runner" % "5.1.1.0"   % "test",
  "org.scalatest"              %% "scalatest"                    % "3.0.1"     % "test",
  "junit"                      %  "junit"                        % "4.12"      % "test",
  "com.novocode"               %  "junit-interface"              % "0.11"      % "test"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

scalacOptions := Seq("-deprecation", "-feature")

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/takezoe/akka-stream-elasticsearch</url>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/takezoe/tranquil</url>
    <connection>scm:git:https://github.com/takezoe/akka-stream-elasticsearch.git</connection>
  </scm>
  <developers>
    <developer>
      <id>takezoe</id>
      <name>Naoki Takezoe</name>
      <email>takezoe_at_gmail.com</email>
      <timezone>+9</timezone>
    </developer>
  </developers>
)
