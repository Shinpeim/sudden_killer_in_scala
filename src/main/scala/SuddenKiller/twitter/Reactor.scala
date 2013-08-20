package SuddenKiller.twitter

import twitter4j.conf.Configuration
import twitter4j.{User, TwitterFactory, Status}
import org.apache.logging.log4j.LogManager
import SuddenKiller.Suddenizer

class Reactor(config: Configuration) {
  val log = LogManager.getLogger(this.getClass)
  val client = (new TwitterFactory(config)).getInstance
  val interval = 60 * 15 * 1000 //msec.
  var keepSilentUntil = System.currentTimeMillis + interval

  def reactToStatus(status:Status) = status match {
    case s if s.getUser.isProtected => Unit
    case s if s.isRetweet => Unit
    case s if s.getText.matches(".*@totsuzenshi_bot.*unfollow.*") => unfollow(s)
    case s => suddenize(s)
  }

  private def suddenize(s: Status) = Suddenizer.suddenize(s.getText) match {
    case Some(suddenized) if suddenized.size > 140 => Unit
    case Some(suddenized) if suddenized.size < 40  => Unit
    case Some(suddenized) if suddenized.contains("@") => Unit
    case Some(suddenized) if suddenized.contains("http") => Unit
    case Some(suddenized) => tweet(suddenized)
    case None => Unit
  }

  private def unfollow(s: Status) = try {
    log.info("unfollowing...: " + s.getUser.getScreenName)
    client.destroyFriendship(s.getUser.getId)
    log.info("unfollowed...: " + s.getUser.getScreenName)
  } catch {
    case (e: Throwable) => log.warn(e)
  }

  private def tweet(status: String) = {
    if (System.currentTimeMillis > keepSilentUntil) {
      client.updateStatus(status)
      keepSilentUntil = System.currentTimeMillis + interval
    }
  }

  def reactToFollow(source: User) = {
    if ( !source.isProtected && source.getScreenName != "totsuzenshi_bot") followBack(source)
  }

  private def followBack(user: User) = try {
    log.info("followbacking...: " + user.getScreenName)
    client.createFriendship(user.getId)
    log.info("followbacked: " + user.getScreenName)
  } catch {
    case (e: Throwable) => log.warn(e)
  }
}
