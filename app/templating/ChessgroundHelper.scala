package lila.app
package templating

//TODO: Muddled Pos here, chess specific stuff in here with ranks/files
import strategygames.Pos
import strategygames.chess
import strategygames.draughts
import strategygames.{ Board, Color, History }
import lila.api.Context

import lila.app.ui.ScalatagsTemplate._
import lila.game.Pov

trait ChessgroundHelper {

  private val cgWrap      = div(cls := "cg-wrap")
  private val cgHelper    = tag("cg-helper")
  private val cgContainer = tag("cg-container")
  private val cgBoard     = tag("cg-board")
  val cgWrapContent       = cgHelper(cgContainer(cgBoard))

  def chessground(board: chess.Board, orient: Color, lastMove: List[chess.Pos] = Nil)(implicit ctx: Context): Frag =
    wrap {
      cgBoard {
        raw {
          if (ctx.pref.is3d) ""
          else {
            def top(p: chess.Pos)  = orient.fold(7 - p.rank.index, p.rank.index) * 12.5
            def left(p: chess.Pos) = orient.fold(p.file.index, 7 - p.file.index) * 12.5
            val highlights = ctx.pref.highlight ?? lastMove.distinct.map { pos =>
              s"""<square class="last-move" style="top:${top(pos)}%;left:${left(pos)}%"></square>"""
            } mkString ""
            val pieces =
              if (ctx.pref.isBlindfold) ""
              else
                  board.pieces.map {
                    case (pos, piece) =>
                      val klass = s"${piece.color.name} ${piece.role.name}"
                      s"""<piece class="$klass" style="top:${top(pos)}%;left:${left(pos)}%"></piece>"""
                  } mkString ""
            s"$highlights$pieces"
          }
        }
      }
    }

  def draughtsground(board: draughts.Board, orient: Color, lastMove: List[draughts.Pos] = Nil)(implicit ctx: Context): Frag = wrap {
    cgBoard {
      raw {
        def addX(p: draughts.PosMotion) = if (p.y % 2 != 0) -0.5 else -1.0
        def top(p: draughts.PosMotion) = orient.fold(p.y - 1, 10 - p.y) * 10.0
        def left(p: draughts.PosMotion) = orient.fold(addX(p) + p.x, 4.5 - (addX(p) + p.x)) * 20.0
        val highlights = ctx.pref.highlight ?? lastMove.distinct.map { pos =>
          val pm = board.posAt(pos)
          s"""<square class="last-move" style="top:${top(pm)}%;left:${left(pm)}%"></square>"""
        } mkString ""
        val pieces =
          if (ctx.pref.isBlindfold) ""
          else board.pieces.map {
            case (pos, piece) =>
              val klass = s"${piece.color.name} ${piece.role.name}"
              val pm = board.posAt(pos)
              s"""<piece class="$klass" style="top:${top(pm)}%;left:${left(pm)}%"></piece>"""
          } mkString ""
        s"$highlights$pieces"
      }
    }
  }

  def chessground(pov: Pov)(implicit ctx: Context): Frag =
    (pov.game.board, pov.game.history) match {
      case (Board.Chess(board), History.Chess(history)) =>
        chessground(
          board = board,
          orient = pov.color,
          lastMove = history.lastMove.map(_.origDest) ?? {
            case (orig, dest) => List(orig, dest)
          }
        )
      case (Board.Draughts(board), History.Draughts(history)) =>
        draughtsground(
          board = board,
          orient = pov.color,
          lastMove = history.lastMove.map(_.origDest) ?? {
            case (orig, dest) => List(orig, dest)
          }
        )
      case _ => sys.error("Mismatched board and history")
    }


  private def wrap(content: Frag): Frag =
    cgWrap {
      cgHelper {
        cgContainer {
          content
        }
      }
    }

  lazy val chessgroundBoard = wrap(cgBoard)
}
