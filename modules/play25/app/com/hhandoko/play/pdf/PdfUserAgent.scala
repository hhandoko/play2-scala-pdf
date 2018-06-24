/**
 * File     : PdfUserAgent.scala
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
 *
 * Notes:
 *   Also based on https://github.com/sihil/sihilsia/blob/master/app/controllers/pdf.scala
 */
package com.hhandoko.play.pdf

import java.io._

import com.lowagie.text.Image
import org.xhtmlrenderer.pdf.{ITextFSImage, ITextOutputDevice, ITextUserAgent}
import org.xhtmlrenderer.resource.{CSSResource, ImageResource, XMLResource}

import play.api.Environment

/**
 * Custom iText user agent implementation.
 *
 * @param env The current Play app Environment context.
 * @param outputDevice the abstract output device for PDF generation.
 */
class PdfUserAgent(env: Environment, outputDevice: ITextOutputDevice) extends ITextUserAgent(outputDevice) {

  /**
   * Get an image resource given its URI.
   *
   * @param uri the image resource URI.
   * @return Image resource object.
   */
  override def getImageResource(uri: String): ImageResource = {
    env.resourceAsStream(uri).fold(super.getImageResource(uri)) { toImageResource(uri) }
  }

  /**
   * Get a CSS resource given its URI.
   *
   * @param uri the CSS resource URI.
   * @return CSS resource object.
   */
  override def getCSSResource(uri: String): CSSResource = {
    env.resourceAsStream(uri).fold(super.getCSSResource(uri)) { toCssResource }
  }

  /**
   * Get an XML resource given its URI.
   *
   * @param uri the XML resource URI.
   * @return XML resource object.
   */
  override def getXMLResource(uri: String): XMLResource = {
    env.resourceAsStream(uri).fold(super.getXMLResource(uri)) { XMLResource.load }
  }

  /**
   * Get a binary resource given its URI.
   *
   * @param uri the binary resource URI.
   * @return Binary resource as bytearray.
   */
  override def getBinaryResource(uri: String): Array[Byte] = {
    env.resourceAsStream(uri).fold(super.getBinaryResource(uri)) { toByteArray }
  }

  /**
   * Converts input stream with URI reference to image resource.
   *
   * @param uri the image resource URI.
   * @param stream the input stream to convert.
   * @return Flying Saucer's Image resource.
   */
  private def toImageResource(uri: String)(stream: InputStream): ImageResource = {
    val image = Image.getInstance(toByteArray(stream))
    scaleToOutputResolution(image)
    new ImageResource(uri, new ITextFSImage(image))
  }

  /**
   * Converts input stream to CSS resource.
   *
   * @param stream the input stream to convert.
   * @return Flying Saucer's CSS resource.
   */
  private def toCssResource(stream: InputStream): CSSResource = new CSSResource(stream)

  /**
   * Converts input stream to bytearray.
   *
   * @param stream the input stream to convert.
   * @return Converted input stream as bytearray.
   */
  private def toByteArray(stream: InputStream): Array[Byte] = {
    val buffer = new BufferedInputStream(stream)
    Stream.continually(buffer.read).takeWhile(-1 !=).map(_.toByte).toArray
  }

  /**
   * Scale images to output resolution.
   *
   * @param image the Image object to scale.
   */
  private def scaleToOutputResolution(image: Image): Unit = {
    val factor: Float = getSharedContext.getDotsPerPixel
    image.scaleAbsolute(image.getPlainWidth * factor, image.getPlainHeight * factor)
  }

}
