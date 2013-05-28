package com.nekogata.SuddenKiller.twitter {
  import twitter4j.{UserStreamAdapter, Status}
  import com.nekogata.SuddenKiller.Suddenizer

  class StreamHandler extends UserStreamAdapter{
    override def onStatus(status: Status) = {
      val text = status.getText
      Suddenizer.suddenize(text) match {
        case Some(suddenized) if suddenized.size < 140 => tweet(suddenized)
        case _ => Unit
      }
    }

    private def tweet(status: String) = println(status)
  }
}
