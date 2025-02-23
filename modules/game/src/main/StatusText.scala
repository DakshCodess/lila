package lila.game

import strategygames.variant.Variant
import strategygames.{ Color, Status }

object StatusText {

  import Status._

  def apply(status: Status, win: Option[Color], variant: Variant): String =
    status match {
      case Aborted                  => "Game was aborted."
      case Mate                     => s"${winner(win)} wins by checkmate."
      case PerpetualCheck           => s"${winner(win)} wins by opponent causing perpetual check."
      case Resign                   => s"${loser(win)} resigns."
      case UnknownFinish            => s"${winner(win)} wins."
      case Stalemate if !variant.stalemateIsDraw => s"${winner(win)} wins by stalemate."
      case Stalemate                => "Draw by stalemate."
      case Timeout if win.isDefined => s"${loser(win)} left the game."
      case Timeout | Draw           => "The game is a draw."
      case Outoftime                => s"${winner(win)} wins on time."
      case NoStart                  => s"${loser(win)} wins by forfeit."
      case Cheat                    => "Cheat detected."
      case VariantEnd =>
        variant match {
          case Variant.Chess(strategygames.chess.variant.KingOfTheHill)
            => s"${winner(win)} brings the king in the center."
          case Variant.Chess(strategygames.chess.variant.ThreeCheck)
            => s"${winner(win)} gives the third check."
          case Variant.Chess(strategygames.chess.variant.FiveCheck)
            => s"${winner(win)} gives the fifth check."
          case Variant.Chess(strategygames.chess.variant.RacingKings)
            => s"${winner(win)} wins the race."
          case Variant.Chess(strategygames.chess.variant.LinesOfAction)
            => s"${winner(win)} connects all of their pieces."
          case Variant.Draughts(strategygames.draughts.variant.Breakthrough)
            => s"${winner(win)} has a promotion first."
          case _ => "Game ends by variant rule."
        }
      case _ => ""
    }

  def apply(game: lila.game.Game): String = apply(game.status, game.winnerColor, game.variant)

  private def winner(win: Option[Color]) = win.??(_.toString)
  private def loser(win: Option[Color])  = winner(win.map(!_))
}
