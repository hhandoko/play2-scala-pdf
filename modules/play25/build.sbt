/**
 * File     : build.sbt
 * License  :
 *   The MIT License (MIT)
 *
 *   Original   - Copyright (c) 2014 Jöerg Viola, Marco Sinigaglia
 *   Derivative - Copyright (c) 2016 - 2018 play2-scala-pdf Contributors
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */
name := """play2-scala-pdf"""

organization := "com.hhandoko"

version := "3.0.0.P25-SNAPSHOT"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.8")

libraryDependencies ++= Seq(
  // Apache Commons IO
  //   - https://commons.apache.org/proper/commons-io/
  "commons-io" % "commons-io" % "2.5",

  // HTML parsing + PDF generation
  //   - http://jtidy.sourceforge.net/
  //   - https://github.com/flyingsaucerproject/flyingsaucer
  //   - https://about.validator.nu/htmlparser/
  "net.sf.jtidy" % "jtidy" % "r938",
  "org.xhtmlrenderer" % "flying-saucer-pdf-itext5" % "9.1.6",
  "nu.validator.htmlparser" % "htmlparser" % "1.4",

  // ScalaTest + Play plugin
  //   - http://www.scalatest.org/plus/play
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

lazy val play25 = (project in file(".")).enablePlugins(PlayScala)
