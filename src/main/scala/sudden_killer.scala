import com.atilika.kuromoji._
import collection.JavaConversions._

object SuddenKiller {

    val tokenizer = Tokenizer.builder().build()
    def main(args: Array[String]) : Unit = {
        args.firstOption match {
            case None    => println("pleas pass a sentence")
            case Some(s) => parse(s)
        }
    }

    def parse(s: String) : Unit = {
        val tokens:List[Token] = tokenizer.tokenize(s).toList
        val words:List[String] = tokens.map(t => t.getSurfaceForm + ":" + t.getAllFeatures)
        words.foreach(println)
    }
}
