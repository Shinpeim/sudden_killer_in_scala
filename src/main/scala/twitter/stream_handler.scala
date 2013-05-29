package com.nekogata.SuddenKiller.twitter {
  import twitter4j.{TwitterFactory, UserStreamAdapter, Status, User}
  import com.nekogata.SuddenKiller.Suddenizer
  import com.nekogata.SuddenKiller.twitter.{Configuration => SuddenKillerConfiguration}
  import scala.util.matching.Regex
  import org.apache.logging.log4j.{Logger, LogManager}

  class StreamHandler(configuration: SuddenKillerConfiguration) extends UserStreamAdapter {
    val log = LogManager.getLogger("StreamHandler")
    val twitter = (new TwitterFactory(configuration.getTwitterConfig)).getInstance
    val interval = 60 * 15 * 1000 //msec.
    var keepSilentUntil = System.currentTimeMillis + interval

    override def onStatus(status: Status) = {
      val user = status.getUser
      val text = status.getText

      log.debug("recieve status: " + user.getScreenName + text)

      text match {
        case s if s.matches(".*@totsuzenshi_bot.*unfollow.*") => unfollow(status.getUser)
        case s if s.matches(".*@.*") => Unit // ignore if text include reply to someone
        case s if s.matches(".*RT.*") => Unit // ignore if retweeted tweet
        case s => suddenize(s)
      }
    }

    override def onFollow(source: User, followedUser: User) = {
      log.debug("recieve follow event: " + source.getScreenName + " follows " + followedUser.getScreenName)
      if ( !source.isProtected && source.getScreenName != "totsuzenshi_bot") followBack(source)
    }

    private def suddenize(text: String) = Suddenizer.suddenize(text) match {
      case Some(suddenized) if suddenized.size < 140 => tweet(suddenized)
      case _ => Unit
    }

    private def tweet(status: String) = {
      if (System.currentTimeMillis > keepSilentUntil) {
        twitter.updateStatus(status)
        keepSilentUntil = System.currentTimeMillis + interval
      }
    }

    private def followBack(user: User) = try {
      log.info("followbacking...: " + user.getScreenName)
      twitter.createFriendship(user.getId)
      log.info("followbacked: " + user.getScreenName)
    } catch {
      case (e: Throwable) => log.warn(e)
    }

    private def unfollow(user: User) = try {
      twitter.destroyFriendship(user.getId)
    } catch {
      case (e: Throwable) => log.warn(e)
    }
  }
}
