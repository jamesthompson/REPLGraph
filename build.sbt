import AssemblyKeys._

assemblySettings

name := "REPL Graph"

version := "0.1a"

scalaVersion := "2.10.1"

javaHome := Some(new File("/Library/Java/JavaVirtualMachines/1.7.0.jdk/Contents/Home"))

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

mainClass in (Compile, run) := Some("replgraph.Launch")

unmanagedJars in Compile += Attributed.blank(file("/Library/Java/JavaVirtualMachines/1.7.0.jdk/Contents/Home/jre/lib/jfxrt.jar"))

libraryDependencies += "org.spire-math" %% "spire" % "0.3.0"

jarName in assembly := "replgraph.jar"

retrieveManaged := true