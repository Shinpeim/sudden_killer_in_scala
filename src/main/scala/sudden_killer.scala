import com.atilika.kuromoji._
import collection.JavaConversions._

case class Word(surface: String, partOfSpeach: String)

object SuddenKiller {
  val tokenizer = Tokenizer.builder().build()

  def main(args: Array[String]) : Unit = {
    val input:String = args.firstOption.getOrElse("")
    val suddenized: String = suddenize(input).getOrElse(input)
    println(suddenized)
  }

  def suddenize(s:String): Option[String] = {
    val reversedWords: List[Word] = tokenize(s).reverse

    val wordsToString: List[Word] => String = words => words.foldLeft(""){(a,b) => a + b.surface}
    val addSuddenDeath: String => String = s => s + """
＿人人人人人人＿
＞　突然の死　＜
￣ＹＹＹＹＹＹ￣
"""

    matchedReversedWords(reversedWords).map(_.reverse).map(wordsToString).map(addSuddenDeath)
  }

  def tokenize(s: String) : List[Word] = {
    def takePartOfString(allFeatures: String) = {
      allFeatures.split(',').first
    }

    val tokens:List[Token] = tokenizer.tokenize(s).toList
    tokens.map(t => Word(t.getSurfaceForm, takePartOfString(t.getAllFeatures)))
  }

  def matchedReversedWords(reversedWords: List[Word]): Option[List[Word]] = reversedWords match {
    case Nil => None
    case Word("の", "助詞") :: Word(_, "名詞") :: _ |
         Word("が", "助詞") :: Word(_, "名詞") :: _ => Some(reversedWords)
    case _ :: rest => matchedReversedWords(rest)
  }
}
