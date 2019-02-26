/**
 * File     : PdfGenerator.scala
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
 *
 * Notes:
 *   Also based on https://github.com/sihil/sihilsia/blob/master/app/controllers/pdf.scala
 */
package com.hhandoko.play.pdf

import java.io._
import scala.collection.mutable.ArrayBuffer

import com.google.inject.Singleton
import com.lowagie.text.pdf.BaseFont
import nu.validator.htmlparser.dom.HtmlDocumentBuilder
import org.apache.commons.io.{FilenameUtils, IOUtils}
import org.w3c.tidy.Tidy
import org.xhtmlrenderer.pdf.ITextRenderer

import play.api.Play
import play.api.mvc.{Result, Results}
import play.twirl.api.Html

/**
 * PDF generator service.
 *
 * @param xhtml true to set XHTML strict parsing (i.e. leave disabled for HTML5 templates).
 */
@Singleton
class PdfGenerator(val xhtml: Boolean = false) {

  /** HTML tidy checker / prettyprinter instance for XHTML strict parsing */
  private lazy val tidyParser = {
    val t = new Tidy()
    t.setXHTML(true)
    t
  }

  /** Default fonts */
  private var defaultFonts = ArrayBuffer[String]()

  /**
   * Load a list of fonts as temporary fonts (will be deleted when application exits) for PDF generation.
   * @note Existing default fonts collection will be cleared / emptied.
   *
   * @param fonts the list of font filenames to load.
   */
  def loadTemporaryFonts(fonts: Seq[String]): Unit = {
    defaultFonts.clear()
    addTemporaryFonts(fonts)
  }

  /**
   * Add a list of fonts as temporary fonts (will be deleted when application exits) for PDF generation.
   *
   * @param fonts the list of font filenames to add.
   */
  def addTemporaryFonts(fonts: Seq[String]): Unit = {
    fonts.foreach { font =>
      val stream = Play.current.resourceAsStream(font)

      stream.map { s =>
        val file = File.createTempFile("tmp_" + FilenameUtils.getBaseName(font), "." + FilenameUtils.getExtension(font))
        file.deleteOnExit()
        val output = new FileOutputStream(file)
        IOUtils.copy(s, output)
        defaultFonts += file.getAbsolutePath
      }
    }
  }

  /**
   * Load a list of local fonts for PDF generation.
   * @note Existing default fonts collection will be cleared / emptied.
   *
   * @param fonts the list of font filenames to load.
   */
  def loadLocalFonts(fonts: Seq[String]): Unit = {
    defaultFonts.clear()
    addLocalFonts(fonts)
  }

  /**
   * Add a list of local fonts for PDF generation.
   *
   * @param fonts the list of font filenames to load.
   */
  def addLocalFonts(fonts: Seq[String]): Unit = defaultFonts ++= fonts

  /**
   * Returns PDF result from Twirl HTML.
   *
   * @param html the Twirl HTML.
   * @param documentBaseUrl the document / page base URL.
   * @param fonts the external / additional fonts to load.
   * @return Generated PDF result (with "application/pdf" MIME type).
   */
  def ok(html: Html, documentBaseUrl: String, fonts: Seq[String] = defaultFonts): Result = {
    Results.Ok(toBytes(parseString(html), documentBaseUrl, fonts)).as("application/pdf")
  }

  /**
   * Generate PDF bytearray given Twirl HTML.
   *
   * @param html the Twirl HTML.
   * @param documentBaseUrl the document / page base URL.
   * @param fonts the external / additional fonts to load.
   * @return Generated PDF as bytearray.
   */
  def toBytes(html: Html, documentBaseUrl: String, fonts: Seq[String]): Array[Byte] = {
    // NOTE: Use default value assignment in method body,
    //       as Scala compiler does not like overloaded methods with default params
    val loadedFonts = if (fonts.isEmpty) defaultFonts else fonts
    toBytes(parseString(html), documentBaseUrl, loadedFonts)
  }

  /**
   * Generate PDF bytearray given HTML string.
   *
   * @param string the HTML string.
   * @param documentBaseUrl the document / page base URL.
   * @param fonts the external / additional fonts to load.
   * @return Generated PDF as bytearray.
   */
  def toBytes(string: String, documentBaseUrl: String, fonts: Seq[String]): Array[Byte] = {
    // NOTE: Use default value assignment in method body,
    //       as Scala compiler does not like overloaded methods with default params
    val loadedFonts = if (fonts.isEmpty) defaultFonts else fonts
    val output = new ByteArrayOutputStream()
    toStream(output)(string, documentBaseUrl, loadedFonts)
    output.toByteArray
  }

  /**
   * Generate and write PDF to an existing OutputStream given HTML string.
   *
   * @param output the OutputStream to write the generated PDF to.
   * @param string the HTML string.
   * @param documentBaseUrl the document / page base URL.
   * @param fonts the external / additional fonts to load.
   */
  def toStream(output: OutputStream)(string: String, documentBaseUrl: String, fonts: Seq[String] = defaultFonts): Unit = {
    val input = new ByteArrayInputStream(string.getBytes("UTF-8"))
    val renderer = new ITextRenderer()
    fonts.foreach { font => renderer.getFontResolver.addFont(font, BaseFont.IDENTITY_H, BaseFont.EMBEDDED) }
    val userAgent = new PdfUserAgent(renderer.getOutputDevice)
    userAgent.setSharedContext(renderer.getSharedContext)
    renderer.getSharedContext.setUserAgentCallback(userAgent)
    val document = new HtmlDocumentBuilder().parse(input)
    renderer.setDocument(document, documentBaseUrl)
    renderer.layout()
    renderer.createPDF(output)
  }

  /**
   * Parse Twirl HTML into HTML string.
   *
   * @param html the generated Twirl HTML.
   * @return HTML as string.
   */
  def parseString(html: Html): String = {
    if (xhtml) {
      val reader = new StringReader(html.body)
      val writer = new StringWriter()
      tidyParser.parse(reader, writer)
      writer.getBuffer.toString
    } else html.body
  }

}
