package build

import com.typesafe.tools.mima.core._
import com.typesafe.tools.mima.core.ProblemFilters._

object BinaryIncompatibilities {
  val IR = Seq(
  )

  val Linker = Seq(
      // Breaking in stable API. OK in Minor version.
      exclude[Problem]("org.scalajs.linker.standard.*"),

      // New method in sealed trait, not an issue.
      exclude[ReversedMissingMethodProblem](
          "org.scalajs.linker.MemOutputDirectory.fileNames"),
  )

  val LinkerInterface = Seq(
  )

  val SbtPlugin = Seq(
  )

  val TestCommon = Seq(
  )

  val TestAdapter = TestCommon ++ Seq(
  )

  val Library = Seq(
  )

  val TestInterface = Seq(
  )
}
