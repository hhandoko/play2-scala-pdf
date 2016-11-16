/**
 * File     : build.sbt
 * License  :
 *   The MIT License (MIT)
 *
 *   Original   - Copyright (c) 2014 Jöerg Viola, Marco Sinigaglia
 *   Derivative - Copyright (c) 2016 Citadel Technology Solutions Pte Ltd
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

organization := "com.builtamont"

version := "1.6.0.P24-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  // Apache Commons IO
  //   - https://commons.apache.org/proper/commons-io/
  "commons-io" % "commons-io" % "2.5",

  // HTML parsing + PDF generation
  //   - http://jtidy.sourceforge.net/
  //   - https://code.google.com/archive/p/flying-saucer/
  //   - https://about.validator.nu/htmlparser/
  "net.sf.jtidy" % "jtidy" % "r938",
  "org.xhtmlrenderer" % "flying-saucer-pdf" % "9.0.9",
  "nu.validator.htmlparser" % "htmlparser" % "1.4",

  // ScalaTest + Play plugin
  //   - http://www.scalatest.org/plus/play
  "org.scalatestplus" %% "play" % "1.4.0" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

lazy val play24 = (project in file(".")).enablePlugins(PlayScala)
