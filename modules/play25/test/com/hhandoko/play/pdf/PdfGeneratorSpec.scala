/**
 * File     : PdfGeneratorSpec.scala
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
package com.hhandoko.play.pdf

import java.io.InputStream
import scala.io.Source

import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.parser.PdfTextExtractor
import org.jsoup.Jsoup
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}

import play.api.Environment
import play.twirl.api.Html

class PdfGeneratorSpec extends PlaySpec with OneAppPerTest {

  val BASE_URL = "http://localhost:9001"

  def env: Environment = app.injector.instanceOf[Environment]
  def htmlStream: Option[InputStream] = env.resourceAsStream("example.html")
  def htmlString: String = htmlStream.fold("") { Source.fromInputStream(_).getLines().mkString("\n") }
  def html: Html = Html(htmlString)
  def gen = new PdfGenerator(env)
  def clean(html: String): String = Jsoup.parse(html).text()

  "PdfGenerator" should {

    val author = "play2-scala-pdf Contributors"
    val title = "`play2-scala-pdf` HTML to PDF example"
    val heading = "Hello, world!"

    "be able to create PDF given HTML string" in {
      val pdf = gen.toBytes(htmlString, BASE_URL, Nil)
      val reader = new PdfReader(pdf)
      val extractor = new PdfTextExtractor(reader)

      assert(reader.getInfo.get("Author") === author)
      assert(reader.getInfo.get("Title") === title)
      assert(clean(extractor.getTextFromPage(1)).contains(heading))
    }

    "be able to create PDF given HTML string and external font" in {
      val fonts = Seq("opensans-regular.ttf")
      val pdf = gen.toBytes(htmlString, BASE_URL, fonts)
      val reader = new PdfReader(pdf)
      val extractor = new PdfTextExtractor(reader)

      assert(reader.getInfo.get("Author") === author)
      assert(reader.getInfo.get("Title") === title)
      assert(clean(extractor.getTextFromPage(1)).contains(heading))
    }

    "be able to create PDF given Twirl HTML" in {
      val pdf = gen.toBytes(html, BASE_URL, Nil)
      val reader = new PdfReader(pdf)
      val extractor = new PdfTextExtractor(reader)

      assert(reader.getInfo.get("Author") === author)
      assert(reader.getInfo.get("Title") === title)
      assert(clean(extractor.getTextFromPage(1)).contains(heading))
    }

    "be able to create PDF given Twirl HTML and external font" in {
      val fonts = Seq("opensans-regular.ttf")
      val pdf = gen.toBytes(html, BASE_URL, fonts)
      val reader = new PdfReader(pdf)
      val extractor = new PdfTextExtractor(reader)

      assert(reader.getInfo.get("Author") === author)
      assert(reader.getInfo.get("Title") === title)
      assert(clean(extractor.getTextFromPage(1)).contains(heading))
    }

  }

}
