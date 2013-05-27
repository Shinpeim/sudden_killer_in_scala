import com.atilika.kuromoji._
import collection.JavaConversions._

case class Word(surface: String, partOfSpeach: String)

object SuddenKiller {
  val tokenizer = Tokenizer.builder().build()

  def main(args: Array[String]) : Unit = {
    val words = parse(args.first)
    words.foreach(w => println(w.surface + ":" + w.partOfSpeach))
  }

  def parse(s: String) : List[Word] = {
    def takePartOfString(allFeatures: String) = {
      allFeatures.split(',').first
    }

    val tokens:List[Token] = tokenizer.tokenize(s).toList
    tokens.map(t => Word(t.getSurfaceForm, takePartOfString(t.getAllFeatures)))
  }
}
