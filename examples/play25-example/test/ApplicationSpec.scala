/**
 * File     : ApplicationSpec.scala
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
import org.scalatestplus.play._

import play.api.test._
import play.api.test.Helpers._

import com.builtamont.controllers.routes

class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status) mustBe Some(NOT_FOUND)
    }

  }

  "HomeController" should {

    "render the index page" in {
      val Some(home) = route(app, FakeRequest(GET, routes.HomeController.index().url))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Hello, world!")
    }

    "render the example page as HTML" in {
      val Some(exampleHtml) = route(app, FakeRequest(GET, routes.HomeController.exampleHtml().url))

      status(exampleHtml) mustBe OK
      contentType(exampleHtml) mustBe Some("text/html")
      contentAsString(exampleHtml) must include ("Example page for `play2-scala-pdf`")
    }

    "render the example page as PDF" in {
      val Some(exampleHtml) = route(app, FakeRequest(GET, routes.HomeController.examplePdf().url))

      status(exampleHtml) mustBe OK
      contentType(exampleHtml) mustBe Some("application/pdf")
    }

  }

}
