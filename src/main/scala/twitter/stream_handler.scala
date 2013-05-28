package com.nekogata.SuddenKiller.twitter {
  import twitter4j.{TwitterFactory, UserStreamAdapter, Status, User}
  import com.nekogata.SuddenKiller.Suddenizer
  import com.nekogata.SuddenKiller.twitter.{Configuration => SuddenKillerConfiguration}
  import scala.util.matching.Regex

  class StreamHandler(configuration: SuddenKillerConfiguration) extends UserStreamAdapter {
    val twitter = (new TwitterFactory(configuration.getTwitterConfig)).getInstance
    val interval = 60 * 3 * 1000 //msec.
    var keepSilentUntil = System.currentTimeMillis + interval

    override def onStatus(status: Status) = status.getText match {
      case s if s.matches(".*@totsuzenshi_bot.*unfollow.*") => unfollow(status.getUser)
      case s if s.matches(".*@.*") => Unit // ignore if text include reply to someone
      case s => suddenize(s)
    }

    override def onFollow(source: User, followedUser: User) = {
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
      twitter.createFriendship(user.getId)
    } catch {
      case (e: Throwable) => println(e)
    }

    private def unfollow(user: User) = try {
      twitter.destroyFriendship(user.getId)
    } catch {
      case (e: Throwable) => println(e)
    }
  }
}
