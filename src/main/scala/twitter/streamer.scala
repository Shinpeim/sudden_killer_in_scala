package com.nekogata.SuddenKiller.twitter {
  import twitter4j.TwitterStreamFactory
  import twitter4j.conf.{Configuration => TwitterConfiguration}
  import com.nekogata.SuddenKiller.twitter.{StreamHandler => SuddenKillerStreamhandler, Configuration => SuddenKillerConfiguration}

  class Streamer(config: SuddenKillerConfiguration, handler: StreamHandler) {
    val stream = (new TwitterStreamFactory(config.getTwitterConfig)).getInstance
    stream.addListener(handler)

    def start = stream.user
  }
}
