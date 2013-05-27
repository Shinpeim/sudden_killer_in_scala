import com.atilika.kuromoji._
import collection.JavaConversions._

case class Word(surface: String, partOfSpeach: String)

object SuddenKiller {
  lazy val tokenizer = Tokenizer.builder().build()
  val SUDDEN_DEATH_STRING = """
 ＿人人人人人人＿
＞　突然の死　＜
￣ＹＹＹＹＹＹ￣
"""

  def main(args: Array[String]) = {
    val input = args.firstOption.getOrElse("")
    val suddenized = suddenize(input).getOrElse(input)
    println(suddenized)
  }

  def suddenize(s: String): Option[String] = {
    val words = tokenize(s).map(tokenToWord)
    val wordsToSuddenize = takeReversedWordsToSuddenize(words.reverse).map(_.reverse)
    wordsToSuddenize.map(wordsToString).map(_ + SUDDEN_DEATH_STRING)
  }

  private def tokenize(s: String): List[Token] = tokenizer.tokenize(s).toList
  private def tokenToWord(t: Token): Word = Word(t.getSurfaceForm, t.getAllFeatures.split(',').first)
  private def takeReversedWordsToSuddenize(reversedWords:List[Word]): Option[List[Word]] = reversedWords match {
    case Nil => None
    case Word("の", "助詞") :: Word(_, "名詞") :: _ |
         Word("が", "助詞") :: Word(_, "名詞") :: _ => Some(reversedWords)
    case _ :: rest => takeReversedWordsToSuddenize(rest)
  }
  private def wordsToString(words:List[Word]) = words.foldLeft(""){(a,b) => a + b.surface}
}
