# Play 2.4 PDF module

`play2-scala-pdf` is a Play! module to help generate PDF documents dynamically from Play! web application.

It simply renders Play! HTML and CSS-based view templates to PDF via [Flying Saucer library], which uses older, open-source version of iText for PDF generation.

This project is a fork of [https://github.com/innoveit/play2-pdf](https://github.com/innoveit/play2-pdf). The primary reason for the fork is to reduce the final distribution size for Play! Scala projects by rebasing the module to Play! Scala core (i.e. avoid including Play! Java additions in Play! Scala projects). 

## Installation

Currently, the module is hosted at Maven Central Repository. Include the following lines in ```build.sbt```, then reload SBT to resolve and enable the module:
``` scala
libraryDependencies ++= Seq(
  ...
  "com.builtamont" % "play2-scala-pdf" % "1.5.1"
)
```

*NOTE: If you are using an IDE like Eclipse, remember to re-generate your project files.* 

## Usage

You can use a standard Play! Scala template like this one:
``` html
@(message: String)

@main("Example Page") {
    Image: <img src="/public/images/favicon.png"/><br/>
    Hello world! <br/>
    @message <br/>
}
```

Then this template, after having imported ```com.builtamont.play.pdf.PdfGenerator```, can simply be rendered as:
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

External assets such as images and stylesheets will be loaded via HTTP calls (as per normal HTML page load). However, if the specified URI is a path into Play! app classpath, the resource is loaded directly instead. See the above sample template for an example.

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

`play2-scala-pdf` is released under the MIT license. See the [LICENSE] file for further details.

`Open Sans` font by [Steve Matteson] is released under the Apache 2 license. See the [Open Sans Google Fonts] page for further details.

`Play Framework` logo is a trademark of [Lightbend].

## Releases

https://github.com/builtamont/play2-scala-pdf/releases

[CONTRIBUTING]: CONTRIBUTING.md
[Flying Saucer library]: https://github.com/flyingsaucerproject/flyingsaucer
[fork-and-pull]: https://help.github.com/articles/using-pull-requests
[LICENSE]: LICENSE
[Lightbend]: https://www.lightbend.com/company
[Open Sans Google Fonts]: https://fonts.google.com/specimen/Open+Sans
[Steve Matteson]: https://twitter.com/@SteveMatteson1