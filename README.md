# GatOS 😸
### _Making scripts in Scala much easier!_
![Continuous Integration](https://github.com/typelevel/gatos/workflows/Continuous%20Integration/badge.svg)

## Overview 
GatOS (that's Spanish for Cats!) is a library for working with files (and soon processes) using pure [`IO`](https://typelevel.org/cats-effect/docs/getting-started), designed to make things much easier. 

**Before**
```scala 3
Files[IO]
  .readUtf8(path)
  .evalMap(IO.println(_))
  .compile
  .drain
```

**After**
``` scala 3
path.read >>= IO.println 
```

It does this by drastically reducing the complexity of the `Files' API and getting rid of difficult concepts that are not so necessary for scripting.

## Getting Started

You can use GatOS in a new or existing Scala 2.13.x or 3.x project by adding it to your `build.sbt` file:

```scala
libraryDependencies ++= List(
  "org.typelevel" %% "gatos" % latest
)
```

## Example
GatOS is a library to perform common script operations such as working with processes and files while maintaining referential transparency! 

```scala 3 mdoc:reset
import cats.effect.{IO, IOApp, ExitCode}

import gatos.*
import gatos.syntax.path.*

object Main extends IOApp: 

  def run(args: List[String]): IO[ExitCode] = 
    for
      home   <- userHome
      config = home / ".gatos" / "config.conf"
      _         <- config.createFile
      _         <- config.write("scripting.made.easy = true")
      newconfig <- config.read
      _         <- IO.println(s"Loading config: $newconfig")
    yield ExitCode.Success
    
end Main
```
