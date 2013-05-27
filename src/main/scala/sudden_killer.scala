import com.atilika.kuromoji._
import collection.JavaConversions._

object Suddenizer {
  class Word(surface: String) {
    override val toString = surface
  }
  case class Noun     (surface: String) extends Word(surface)
  case class Particle (surface: String) extends Word(surface)
  case class OtherWord(surface: String) extends Word(surface)

  object Word {
    def fromToken(t: Token): Word = {
      t.getAllFeatures.split(',').first match {
        case "名詞" => Noun(t.getSurfaceForm)
        case "助詞" => Particle(t.getSurfaceForm)
        case _      => OtherWord(t.getSurfaceForm)
      }
    }
  }

  lazy val tokenizer = Tokenizer.builder().build()
  val SUDDEN_DEATH_STRING = """
 ＿人人人人人人＿
＞　突然の死　＜
￣ＹＹＹＹＹＹ￣
"""

  def suddenize(s: String): Option[String] = {
    val words = tokenize(s).map(Word.fromToken)
    takeReversedWordsToSuddenize(words.reverse).map(_.reverse) match {
      case Some(words) => Some(words.map(_.toString).reduce(_ + _) + SUDDEN_DEATH_STRING)
      case None => None
    }
  }

  private def tokenize(s: String): List[Token] = tokenizer.tokenize(s).toList
  private def takeReversedWordsToSuddenize(reversedWords:List[Word]): Option[List[Word]] = reversedWords match {
    case Nil => None
    case Particle("の") :: Noun(_) :: _ |
         Particle("が") :: Noun(_) :: _ => Some(reversedWords)
    case _ :: rest => takeReversedWordsToSuddenize(rest)
  }
}

object SuddenKiller {
  def main(args: Array[String]) = {
    val input = args.firstOption.getOrElse("")
    val suddenized = Suddenizer.suddenize(input).getOrElse(input)
    println(suddenized)
  }
}
