[![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](LICENSE)
[![Master Build Status](https://travis-ci.org/hhandoko/play2-scala-pdf.svg?branch=master)](https://travis-ci.org/hhandoko/play2-scala-pdf)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.hhandoko/play26-scala-pdf_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.hhandoko/play26-scala-pdf_2.12)

# Play [2.4 | 2.5 | 2.6] PDF module

`play2-scala-pdf` is a Play Framework module to help generate PDF documents dynamically from Play Framework web application.

It simply renders Play Framework HTML and CSS-based view templates to PDF via [Flying Saucer library], which uses [OpenPDF], an open-source LGPL and MPL version of an older fork of iText for PDF generation.

### Supported Play Framework and Scala Versions

The supported Scala and Play versions as follows:

|           | Scala 2.10 | Scala 2.11 | Scala 2.12 |
| --------- |:----------:|:----------:|:----------:|
| Play 2.4  | `YES`      | `YES`      |            |
| Play 2.5  |            | `YES`      |            |
| Play 2.6  |            | `YES`      | `YES`      |

### Play Framework Java

If you are using Play Framework Java, check out [https://github.com/innoveit/play2-pdf](https://github.com/innoveit/play2-pdf).

`play2-scala-pdf` is a fork of the project above with the aim to reduce the final distribution size for Play Framework Scala projects by rebasing the module to Play Framework Scala core (i.e. avoid including Play Framework Java additions in Play Framework Scala projects).

## Installation

Create a PDF generator factory method in your application's Guice module:
``` scala
@Provides
def providePdfGenerator(): PdfGenerator = {
  val pdfGen = new PdfGenerator()
  pdfGen.loadLocalFonts(Seq("fonts/opensans-regular.ttf"))
  pdfGen
}
``` 

Currently, the module is hosted at Maven Central Repository. Include the following lines in ```build.sbt```, then reload SBT to resolve and enable the module:
``` scala
libraryDependencies ++= Seq(
  ...
  "com.hhandoko" %% "play26-scala-pdf" % "4.0.0" // Use `play25-scala-pdf` for Play 2.5.x apps or `play24-scala-pdf` for Play 2.4.x apps
)
```

Remember to add Sonatype snapshots repository to use snapshot releases:
``` scala
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
```

*NOTE: If you are using an IDE like Eclipse, remember to re-generate your project files.* 

## Usage

You can use a standard Play Framework Scala template like this one:
``` html
@(message: String)

@main("Example Page") {
    Image: <img src="/public/images/favicon.png"/><br/>
    Hello world! <br/>
    @message <br/>
}
```

Then this template, after having imported ```com.hhandoko.play.pdf.PdfGenerator```, can simply be rendered as:
``` scala
class HomeController @Inject() (pdfGen: PdfGenerator) extends Controller {
    
    def examplePdf = Action { pdfGen.ok(views.html.example(), "http://localhost:9000") }
    
}
```

where ```pdfGenerator.ok``` is a simple shorthand notation for:
``` scala
ok(pdfGenerator.toBytes(document.render("Your new application is ready."), "http://localhost:9000")).as("application/pdf")
```

## Template rules

Please observe the following constraints to avoid issues when using this module:

  - Avoid using `media="screen"` qualifier on linked CSS in `<head>`, otherwise you must provide specific print stylesheets (i.e. `media="print"`)
  - Non-system fonts must be loaded explicitly (see below for example)
  - External assets such as images need to be loaded as base64-encoded string
  - External such as stylesheets will be loaded as per normal HTML page load
  
*NOTE: If the specified URI is a path into Play Framework app classpath, the resource is loaded directly instead. See the above sample template for an example.*

Fonts can be loaded by invoking `PdfGenerator.loadLocalFonts` method. For example:

  - if fonts are stored under `/conf/fonts` (or other project path mapped to the classpath),
  - it can be loaded by invoking `pdfGenerator.loadLocalFonts(Seq("fonts/FreeSans.ttf"))`

*NOTE: Non-system fonts in this context refers to WebFonts, fonts not available to the Java VM, or other fonts not included as part of normal OS distribution*

## Contributing

We follow the "[fork-and-pull]" Git workflow.

  1. Fork the repo on GitHub
  1. Commit changes to a branch in your fork (use `snake_case` convention):
     - For technical chores, use `chore/` prefix followed by the short description, e.g. `chore/do_this_chore`
     - For new features, use `feature/` prefix followed by the feature name, e.g. `feature/feature_name`
     - For bug fixes, use `bug/` prefix followed by the short description, e.g. `bug/fix_this_bug`
  1. Rebase or merge from "upstream"
  1. Submit a PR "upstream" with your changes

Please read [CONTRIBUTING] for more details.

## License

```
The MIT License (MIT)

Original   - Copyright (c) 2014 Jöerg Viola, Marco Sinigaglia
Derivative - Copyright (c) 2016 - 2019 play2-scala-pdf Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

`play2-scala-pdf` is released under the MIT license. See the [LICENSE] file for further details.

`Open Sans` font by [Steve Matteson] is released under the Apache 2 license. See the [Open Sans Google Fonts] page for further details.

`Play Framework` logo is a trademark of [Lightbend].

## Releases

https://github.com/hhandoko/play2-scala-pdf/releases

[CONTRIBUTING]: CONTRIBUTING.md
[Flying Saucer library]: https://github.com/flyingsaucerproject/flyingsaucer
[fork-and-pull]: https://help.github.com/articles/using-pull-requests
[LICENSE]: LICENSE
[Lightbend]: https://www.lightbend.com/company
[OpenPDF]: https://github.com/LibrePDF/OpenPDF
[Open Sans Google Fonts]: https://fonts.google.com/specimen/Open+Sans
[Steve Matteson]: https://twitter.com/@SteveMatteson1
