package com.nekogata.SuddenKiller.twitter {
  import twitter4j.conf.ConfigurationBuilder

  class Configuration(accessToken: String, accessTokenSecret: String, consumerKey: String, consumerSecret: String) {
    val builder = new ConfigurationBuilder
    builder.setOAuthAccessToken(accessToken)
    builder.setOAuthAccessTokenSecret(accessTokenSecret)
    builder.setOAuthConsumerKey(consumerKey)
    builder.setOAuthConsumerSecret(consumerSecret)

    val config = builder.build

    def getTwitterConfig = config
  }
}
