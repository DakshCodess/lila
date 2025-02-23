package lila.lobby

import strategygames.{ Game => StratGame, Situation }

import actorApi.{ JoinHook, JoinSeek }
import lila.game.{ Game, PerfPicker, Player }
import lila.socket.Socket.Sri
import lila.user.User

final private class Biter(
    userRepo: lila.user.UserRepo,
    gameRepo: lila.game.GameRepo
)(implicit
    ec: scala.concurrent.ExecutionContext,
    idGenerator: lila.game.IdGenerator
) {

  def apply(hook: Hook, sri: Sri, user: Option[LobbyUser]): Fu[JoinHook] =
    if (canJoin(hook, user)) join(hook, sri, user)
    else fufail(s"$user cannot bite hook $hook")

  def apply(seek: Seek, user: LobbyUser): Fu[JoinSeek] =
    if (canJoin(seek, user)) join(seek, user)
    else fufail(s"$user cannot join seek $seek")

  private def join(hook: Hook, sri: Sri, lobbyUserOption: Option[LobbyUser]): Fu[JoinHook] =
    for {
      userOption   <- lobbyUserOption.map(_.id) ?? userRepo.byId
      ownerOption  <- hook.userId ?? userRepo.byId
      creatorColor <- assignCreatorColor(ownerOption, userOption, hook.realColor)
      game <- makeGame(
        hook,
        whiteUser = creatorColor.fold(ownerOption, userOption),
        blackUser = creatorColor.fold(userOption, ownerOption)
      ).withUniqueId
      _ <- gameRepo insertDenormalized game
    } yield {
      lila.mon.lobby.hook.join.increment()
      JoinHook(sri, hook, game, creatorColor)
    }

  private def join(seek: Seek, lobbyUser: LobbyUser): Fu[JoinSeek] =
    for {
      user         <- userRepo byId lobbyUser.id orFail s"No such user: ${lobbyUser.id}"
      owner        <- userRepo byId seek.user.id orFail s"No such user: ${seek.user.id}"
      creatorColor <- assignCreatorColor(owner.some, user.some, seek.realColor)
      game <- makeGame(
        seek,
        whiteUser = creatorColor.fold(owner.some, user.some),
        blackUser = creatorColor.fold(user.some, owner.some)
      ).withUniqueId
      _ <- gameRepo insertDenormalized game
    } yield JoinSeek(user.id, seek, game, creatorColor)

  private def assignCreatorColor(
      creatorUser: Option[User],
      joinerUser: Option[User],
      color: Color
  ): Fu[strategygames.Color] =
    color match {
      case Color.Random =>
        userRepo.firstGetsWhite(creatorUser.map(_.id), joinerUser.map(_.id)) map strategygames.Color.fromWhite
      case Color.White => fuccess(strategygames.White)
      case Color.Black => fuccess(strategygames.Black)
    }

  private def makeGame(hook: Hook, whiteUser: Option[User], blackUser: Option[User]) = {
    val clock      = hook.clock.toClock
    val perfPicker = PerfPicker.mainOrDefault(strategygames.Speed(clock.config), hook.realVariant, none)
    Game
      .make(
        chess = StratGame(
          lib = hook.realVariant.gameLogic,
          situation = Situation(hook.realVariant.gameLogic, hook.realVariant),
          clock = clock.some
        ),
        whitePlayer = Player.make(strategygames.White, whiteUser, perfPicker),
        blackPlayer = Player.make(strategygames.Black, blackUser, perfPicker),
        mode = hook.realMode,
        source = lila.game.Source.Lobby,
        pgnImport = None
      )
      .start
  }

  private def makeGame(seek: Seek, whiteUser: Option[User], blackUser: Option[User]) = {
    val perfPicker = PerfPicker.mainOrDefault(strategygames.Speed(none), seek.realVariant, seek.daysPerTurn)
    Game
      .make(
        chess = StratGame(
          lib = seek.realVariant.gameLogic,
          situation = Situation(seek.realVariant.gameLogic, seek.realVariant),
          clock = none
        ),
        whitePlayer = Player.make(strategygames.White, whiteUser, perfPicker),
        blackPlayer = Player.make(strategygames.Black, blackUser, perfPicker),
        mode = seek.realMode,
        source = lila.game.Source.Lobby,
        daysPerTurn = seek.daysPerTurn,
        pgnImport = None
      )
      .start
  }

  def canJoin(hook: Hook, user: Option[LobbyUser]): Boolean =
    hook.isAuth == user.isDefined && user.fold(true) { u =>
      u.lame == hook.lame &&
      !hook.userId.contains(u.id) &&
      !hook.userId.??(u.blocking.contains) &&
      !hook.user.??(_.blocking).contains(u.id) &&
      hook.realRatingRange.fold(true) { range =>
        (hook.perfType map u.ratingAt) ?? range.contains
      }
    }

  def canJoin(seek: Seek, user: LobbyUser): Boolean =
    seek.user.id != user.id &&
      (seek.realMode.casual || user.lame == seek.user.lame) &&
      !(user.blocking contains seek.user.id) &&
      !(seek.user.blocking contains user.id) &&
      seek.realRatingRange.fold(true) { range =>
        (seek.perfType map user.ratingAt) ?? range.contains
      }

  def showHookTo(hook: Hook, member: LobbySocket.Member): Boolean =
    hook.sri == member.sri || canJoin(hook, member.user)
}
