import com.nekogata.SuddenKiller._
import collection.JavaConversions._

object SuddenKiller {
  def main(args: Array[String]) = {
    val input = args.headOption.getOrElse("")
    val suddenized = Suddenizer.suddenize(input).getOrElse(input)
    println(suddenized)
  }
}
