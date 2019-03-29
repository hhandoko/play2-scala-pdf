/**
 * File     : publish.sbt
 * License  :
 *   The MIT License (MIT)
 *
 *   Original   - Copyright (c) 2014 Jöerg Viola, Marco Sinigaglia
 *   Derivative - Copyright (c) 2016 - 2019 play2-scala-pdf Contributors
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
publishMavenStyle := true

pomIncludeRepository := { _ => false }

sonatypeProfileName := "com.hhandoko"

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomExtra in Global := (
  <url>https://github.com/hhandoko/play2-scala-pdf</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>https://raw.githubusercontent.com/hhandoko/play2-scala-pdf/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:hhandoko/play2-scala-pdf.git</url>
    <connection>scm:git:git@github.com:hhandoko/play2-scala-pdf.git</connection>
  </scm>
  <developers>
    <developer>
      <id>hhandoko</id>
      <name>Herdy Handoko</name>
      <url>http://github.com/hhandoko</url>
    </developer>
  </developers>
)
