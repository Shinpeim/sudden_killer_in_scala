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

    type StatusHandler = Function1[Status, Unit]
    type OnStatusPattern = PartialFunction[Status, Option[StatusHandler]]
    val onStatusPattern: OnStatusPattern = {
      case s if s.getUser.isProtected => None
      case s if s.isRetweet => None
      case s if s.getText.matches(".*@.*") => None
      case s if s.getText.matches(".*@totsuzenshi_bot.*unfollow.*") => Some(unfollow)
      case s => Some(suddenize)
    }

    override def onStatus(status: Status) = {
      val user = status.getUser
      val text = status.getText
      log.debug("recieve status: " + user.getScreenName + text)

      onStatusPattern(status).foreach( _(status) )
    }

    override def onFollow(source: User, followedUser: User) = {
      log.debug("recieve follow event: " + source.getScreenName + " follows " + followedUser.getScreenName)
      if ( !source.isProtected && source.getScreenName != "totsuzenshi_bot") followBack(source)
    }

    private def suddenize(s: Status) = Suddenizer.suddenize(s.getText) match {
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

    private def unfollow(s:Status) = try {
      twitter.destroyFriendship(s.getUser.getId)
    } catch {
      case (e: Throwable) => log.warn(e)
    }
  }
}
