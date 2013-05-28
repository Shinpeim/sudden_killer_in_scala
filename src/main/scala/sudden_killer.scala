import com.nekogata.SuddenKiller._
import com.nekogata.SuddenKiller.twitter._
import collection.JavaConversions._

object SuddenKiller {
  def main(args: Array[String]) = {
    val config = new Configuration(args(0), args(1), args(2), args(3))
    new Streamer(config, new StreamHandler(config)).start
  }
}
