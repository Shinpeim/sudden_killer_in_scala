import com.nekogata.suddenizer._

object SuddenKiller {
  def main(args: Array[String]) = {
    val input = args.firstOption.getOrElse("")
    val suddenized = Suddenizer.suddenize(input).getOrElse(input)
    println(suddenized)
  }
}
