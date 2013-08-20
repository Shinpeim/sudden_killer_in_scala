package SuddenKiller.twitter

import twitter4j.{User, Status, UserStreamAdapter}
import org.apache.logging.log4j.LogManager

class Streamer(reactor: Reactor) extends UserStreamAdapter {
  val log = LogManager.getLogger("Streamer")

  override def onStatus(status: Status) = {
    val user = status.getUser
    val text = status.getText
    log.debug("recieve status: " + user.getScreenName + text)

    reactor.reactToStatus(status)
 }

  override def onFollow(source: User, followedUser: User) = {
    log.debug("recieve follow event: " + source.getScreenName + " follows " + followedUser.getScreenName)
    reactor.reactToFollow(source)
  }

}
