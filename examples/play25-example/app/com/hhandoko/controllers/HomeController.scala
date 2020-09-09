/**
 * File     : HomeController.scala
 * License  :
 *   The MIT License (MIT)
 *
 *   Original   - Copyright (c) 2014 Jöerg Viola, Marco Sinigaglia
 *   Derivative - Copyright (c) 2016 - 2020 play2-scala-pdf Contributors
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
package com.hhandoko.controllers

import javax.inject._
import scala.concurrent.ExecutionContext

import play.api.Configuration
import play.api.mvc._

import com.hhandoko.play.pdf.PdfGenerator

/**
 * Home controller.
 *
 * @param config the application configuration
 * @param pdfGen the PDF generator implementation.
 */
@Singleton
class HomeController @Inject() (config: Configuration, pdfGen: PdfGenerator)(implicit exec: ExecutionContext) extends Controller {

  val BASE_URL = config.getString("application.base_url").getOrElse("http://localhost:9000")

  /**
   * Returns the homepage ('/').
   *
   * @return Homepage.
   */
  def index = Action { Ok(views.html.index()) }

  /**
   * Returns the example page as HTML ('/example').
   *
   * @return Example page.
   */
  def exampleHtml = Action { Ok(views.html.example() )}

  /**
   * Returns the example page as PDF document ('/example.pdf').
   *
   * @return Example page as PDF.
   */
  def examplePdf = Action { pdfGen.ok(views.html.example(), BASE_URL) }

}
