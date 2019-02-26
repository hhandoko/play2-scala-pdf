/**
 * File     : PdfUserAgentSpec.scala
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

import scala.io.Source

import org.scalatestplus.play._
import org.xhtmlrenderer.pdf.{ITextRenderer, ITextUserAgent}
import org.xhtmlrenderer.resource.{CSSResource, ImageResource, XMLResource}

import play.api.Environment

class PdfUserAgentSpec extends PlaySpec with OneAppPerTest {

  /**
   * @return Renderer and User Agent.
   */
  def fixture: (ITextRenderer, ITextUserAgent) = {
    val env = app.injector.instanceOf[Environment]
    val r = new ITextRenderer()
    val ua = new PdfUserAgent(env, r.getOutputDevice)
    ua.setSharedContext(r.getSharedContext)
    r.getSharedContext.setUserAgentCallback(ua)
    (r, ua)
  }

  "PdfUserAgent" should {

    "be able to load image resource" in {
      val (_, userAgent) = fixture
      val image = userAgent.getImageResource("play_logo.png")

      assert(image.isInstanceOf[ImageResource])
    }

    "be able to load CSS resource" in {
      val (_, userAgent) = fixture
      val css = userAgent.getCSSResource("example.css")
      val stream = css.getResourceInputSource.getByteStream
      val cssContent = Source.fromInputStream(stream).getLines().mkString("\n")

      assert(css.isInstanceOf[CSSResource])
      assert(cssContent.contains("body { background-color: red; }"))
    }

    "be able to load XML resource" in {
      val (_, userAgent) = fixture
      val xml = userAgent.getXMLResource("example.xml")

      assert(xml.isInstanceOf[XMLResource])
      assert(xml.getDocument.getElementsByTagName("book").getLength === 12)
    }

    "be able to load binary resource" in {
      val (_, userAgent) = fixture
      val file = userAgent.getBinaryResource("play_logo.png")

      assert(file.isInstanceOf[Array[Byte]])
      assert(file.length === 20039)
    }

  }

}
