import org.specs2.mutable._
import com.nekogata.SuddenKiller._
class suddenizerSpec extends Specification {
  val SUDDEN_DEATH_STRING = """
＿人人人人人人＿
＞　突然の死　＜
￣ＹＹＹＹＹＹ￣
"""
  "突然死ナイザーが" should {
    "文章中最後の「名詞 + の」で突然死ナイズすること" in {
       Suddenizer.suddenize("新しいうーねこの鞄うらやましい").get must_==
         "新しいうーねこの" + SUDDEN_DEATH_STRING
    }
    "文章中最後の「名詞 + に(助詞)」で突然死ナイズすること'" in {
      Suddenizer.suddenize("9m さん謹製のリマインダ氏にゴミ出しアラート機能ほしい").get must_==
        "9m さん謹製のリマインダ氏に" + SUDDEN_DEATH_STRING
    }
    "文章中最後の「名詞 + だ(助詞) + と(助詞)」で突然死ナイズすること" in {
      Suddenizer.suddenize("ご飯を食べるときに、あんまり周りが静かだと緊張する").get must_==
        "ご飯を食べるときに、あんまり周りが静かだと" + SUDDEN_DEATH_STRING
    }
    "文章中最後の「動詞 + と(助詞)」で突然死ナイズすること" in {
      Suddenizer.suddenize("おばあさんが川で洗濯をしていると、突然上流から大きな桃が").get must_==
        "おばあさんが川で洗濯をしていると" + SUDDEN_DEATH_STRING
    }
    "文章中最後の「名詞 + で(助詞)」で突然死ナイズすること" in {
      Suddenizer.suddenize("スターバックスもあるし、代々木で待とう").get must_==
        "スターバックスもあるし、代々木で" + SUDDEN_DEATH_STRING
    }
    "文章中最後の「動詞 + けど(助詞)」で突然死ナイズすること" in {
      Suddenizer.suddenize("今日は途中で帰るけど、明日は全参加するわ").get must_==
        "今日は途中で帰るけど" + SUDDEN_DEATH_STRING
    }
    "文章中最後の「動詞 + た (助動詞) + けど(助詞)」で突然死ナイズすること" in {
      Suddenizer.suddenize("昨日は途中で帰ったけど明日は全参加するね").get must_==
        "昨日は途中で帰ったけど" + SUDDEN_DEATH_STRING
    }
    "文章中最後の「名詞 + だ (助動詞) + けど(助詞)」で突然死ナイズすること" in {
      Suddenizer.suddenize("私美少女だけど、こすげさんかっこいいと思う").get must_==
        "私美少女だけど" + SUDDEN_DEATH_STRING
    }
  }
}