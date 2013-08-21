package SuddenKiller
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{TwitterFactory, TwitterStreamFactory}

object SuddenKiller {
  def main(args: Array[String]): Unit = {
    val config = {
      val builder = new ConfigurationBuilder
      builder.setOAuthAccessToken(args(0))
      builder.setOAuthAccessTokenSecret(args(1))
      builder.setOAuthConsumerKey(args(2))
      builder.setOAuthConsumerSecret(args(3))
      builder.build
    }
    val twitterClient = (new TwitterFactory(config)).getInstance

    val reactor = new twitter.Reactor(twitterClient)
    val streamer = new twitter.Streamer(reactor)
    val stream = (new TwitterStreamFactory(config)).getInstance
    stream.addListener(streamer)

    stream.user
  }
}
