import sbt._


class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val twitterNest = "com.twitter" at "http://www.lag.net/nest"
  val android = "org.scala-tools.sbt" % "sbt-android-plugin" % "0.4.2"
}
