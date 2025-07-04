package zio.internal.macros

import scala.util.Try

private[zio] object StringUtils {
  private val UnstyledRegex = "\u001B\\[[;\\d]*m".r

  implicit class StringOps(private val self: String) extends AnyVal {
    def unstyled: String =
      UnstyledRegex.replaceAllIn(self, "")

    def maxLineWidth: Int =
      Try(unstyled.split("\n").map(_.length).max).getOrElse(0)

    /**
     * Joins strings line-wise
     *
     * {{{
     *   s1   +++   s2   ==    result
     * ======     ======    ============
     * line 1     line a    line 1line a
     * line 2     line b    line 2line b
     * line 3     line c    line 3line c
     * }}}
     */
    def +++(that: String): String =
      self
        .split("\n")
        .zipAll(that.split("\n"), "", "")
        .map { case (a, b) => a ++ b }
        .mkString("\n")
  }
}
