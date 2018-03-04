/**
 * File     : IntegrationSpec.scala
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
import org.scalatestplus.play._

import com.hhandoko.controllers.routes

class IntegrationSpec extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory {

  val BASE_URL = "http://localhost:" + port

  "Application" should {

    "work from within a browser" in {
      go to BASE_URL

      pageSource must include ("Hello, world!")
    }

    "load example page as HTML" in {
      go to (BASE_URL + routes.HomeController.exampleHtml().url)

      pageTitle must include ("`play2-scala-pdf` Example | Example Page")
      pageSource must include ("Example page for `play2-scala-pdf`")
    }

    "load example page as PDF" in {
      go to (BASE_URL + routes.HomeController.examplePdf().url)

      pageSource must include ("`play2-scala-pdf` Example | Example Page")
    }

  }
}
