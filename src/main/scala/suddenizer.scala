package com.nekogata.SuddenKiller {
  import org.atilika.kuromoji._
  import scala.collection.JavaConversions._

  object Suddenizer {
    lazy val tokenizer = Tokenizer.builder().build()
    val SUDDEN_DEATH_STRING = """
＿人人人人人人＿
＞　突然の死　＜
￣ＹＹＹＹＹＹ￣
"""

    class Word(surface: String) {
      override val toString = surface
    }
    case class Noun     (surface: String) extends Word(surface)
    case class Particle (surface: String) extends Word(surface)
    case class AuxiliaryVerb(surface: String) extends Word(surface)
    case class Verb     (surface: String) extends Word(surface)
    case class OtherWord(surface: String) extends Word(surface)

    object Word {
      def fromToken(t: Token): Word = {
        t.getAllFeatures.split(',').head match {
          case "名詞"   => Noun(t.getSurfaceForm)
          case "助詞"   => Particle(t.getSurfaceForm)
          case "助動詞" => AuxiliaryVerb(t.getSurfaceForm)
          case "動詞"   => Verb(t.getSurfaceForm)
          case _        => OtherWord(t.getSurfaceForm)
        }
      }
    }

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
      case Particle("で") :: Noun(_) :: _ |
           Particle("けど") :: AuxiliaryVerb("だ") :: Noun(_) :: _ |
           Particle("と") :: Verb(_) :: _ |
           Particle("けど") :: Verb(_) :: _ |
           Particle("けど") :: AuxiliaryVerb("た") :: Verb(_) :: _ |
           Particle("と") :: AuxiliaryVerb("だ") :: Noun(_) :: _
             => Some(reversedWords)
      case _ :: rest => takeReversedWordsToSuddenize(rest)
    }
  }
}
