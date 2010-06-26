import sbt._
import com.twitter.sbt._


class HcwProject(info: ProjectInfo) extends AndroidProject(info) {
  val specs = "org.scala-tools.testing" % "specs" % "1.6.2.1"
  val xrayspecs = "com.twitter" % "xrayspecs" % "1.0.7"  //--auto--

  override def androidPlatformName = "android-7"
}
