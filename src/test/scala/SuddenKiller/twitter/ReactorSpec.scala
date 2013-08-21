package SuddenKiller.twitter

import org.specs2.mutable._
import twitter4j.{User, Twitter, Status}
import org.specs2.mock.Mockito

class ReactorSpec extends Specification with Mockito {
  def getMockedUser(id: Long, isProtected: Boolean = false)= {
    val user = mock[User]
    user.isProtected returns isProtected
    user.getId returns id
    user
  }

  "reactorがfollowを受け取ったとき" should {
    "not protetedなユーザーならfollowbackする" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)
      val user = getMockedUser(1, isProtected = false)
      reactor.reactToFollow(user)

      there was one(mockedClient).createFriendship(1)
    }

    "protetedなユーザーならfollowbackしない" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)
      val user = getMockedUser(1, isProtected = true)
      reactor.reactToFollow(user)

      there was no(mockedClient).createFriendship(1)
    }
  }

  "reactorがtweetを受け取ったとき" should {
    "@totuzenshi_bot unfollow を受け取ったらunfollowする" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]

      status.getUser returns getMockedUser(1, isProtected = true)
      status.getText returns "@totsuzenshi_bot unfollow"
      reactor.reactToStatus(status)

      there was one(mockedClient).destroyFriendship(1)
    }

    "protectedでないユーザーのツイートを突然死ナイズする" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]
      status.getUser returns getMockedUser(1, isProtected = false)
      status.getText returns "雨が降っていて寒いので、スターバックスで待ってます"

      reactor.keepSilentUntil = 0
      reactor.reactToStatus(status)

      there was one(mockedClient).updateStatus(anyString)
    }

    "長過ぎるtweetには反応しない" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]
      status.getUser returns getMockedUser(1, isProtected = false)
      status.getText returns "あ" * 140 + "雨が降っていて寒いので、スターバックスで待ってます"

      reactor.keepSilentUntil = 0
      reactor.reactToStatus(status)

      there was no(mockedClient).updateStatus(anyString)
    }

    "短すぎるtweetには反応しない" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]
      status.getUser returns getMockedUser(1, isProtected = false)
      status.getText returns "スターバックスで待ってます"

      reactor.keepSilentUntil = 0
      reactor.reactToStatus(status)

      there was no(mockedClient).updateStatus(anyString)
    }

    "protectedなユーザには反応しない" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]
      status.getUser returns getMockedUser(1, isProtected = true)
      status.getText returns "雨が降っていて寒いので、スターバックスで待ってます"

      reactor.keepSilentUntil = 0
      reactor.reactToStatus(status)

      there was no(mockedClient).updateStatus(anyString)
    }

    "RTには反応しない" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]
      status.isRetweet returns true
      status.getUser returns getMockedUser(1)
      status.getText returns "雨が降っていて寒いので、スターバックスで待ってます"

      reactor.keepSilentUntil = 0
      reactor.reactToStatus(status)

      there was no(mockedClient).updateStatus(anyString)
    }

    "@を含むtweetには反応しない" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]
      status.getUser returns getMockedUser(1, isProtected = false)
      status.getText returns "@9m 雨が降っていて寒いので、スターバックスで待ってます"

      reactor.keepSilentUntil = 0
      reactor.reactToStatus(status)

      there was no(mockedClient).updateStatus(anyString)
    }

    "httpを含むtweetには反応しない" in {
      val mockedClient = mock[Twitter]
      val reactor = new Reactor(mockedClient)

      val status = mock[Status]
      status.getUser returns getMockedUser(1, isProtected = false)
      status.getText returns "@9m 雨が降っていて寒いので、スターバックスで待ってます http://example.net/"

      reactor.keepSilentUntil = 0
      reactor.reactToStatus(status)

      there was no(mockedClient).updateStatus(anyString)
    }
  }
}

