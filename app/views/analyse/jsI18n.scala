package views.html.analyse

import play.api.i18n.Lang

import lila.app.templating.Environment._
import lila.i18n.{ I18nKeys => trans }

private object jsI18n {

  def apply()(implicit lang: Lang) = i18nJsObject(i18nKeys)

  private val i18nKeys = List(
    trans.flipBoard,
    trans.gameAborted,
    trans.checkmate,
    trans.perpetualCheck,
    trans.whiteResigned,
    trans.blackResigned,
    trans.stalemate,
    trans.whiteLeftTheGame,
    trans.blackLeftTheGame,
    trans.draw,
    trans.whiteTimeOut,
    trans.blackTimeOut,
    trans.playingRightNow,
    trans.whiteIsVictorious,
    trans.blackIsVictorious,
    trans.promotion,
    trans.cheatDetected,
    trans.kingInTheCenter,
    trans.threeChecks,
    trans.fiveChecks,
    trans.checkersConnected,
    trans.variantEnding,
    trans.analysis,
    trans.boardEditor,
    trans.continueFromHere,
    trans.playWithTheMachine,
    trans.playWithAFriend,
    trans.openingExplorer,
    trans.nbInaccuracies,
    trans.nbMistakes,
    trans.nbBlunders,
    trans.averageCentipawnLoss,
    trans.viewTheSolution,
    trans.youNeedAnAccountToDoThat,
    // ceval (also uses gameOver)
    trans.depthX,
    trans.usingServerAnalysis,
    trans.loadingEngine,
    trans.cloudAnalysis,
    trans.goDeeper,
    trans.showThreat,
    trans.gameOver,
    trans.inLocalBrowser,
    trans.toggleLocalEvaluation,
    // action menu
    trans.menu,
    trans.toStudy,
    trans.inlineNotation,
    trans.computerAnalysis,
    trans.enable,
    trans.bestMoveArrow,
    trans.evaluationGauge,
    trans.infiniteAnalysis,
    trans.removesTheDepthLimit,
    trans.multipleLines,
    trans.cpus,
    trans.memory,
    trans.delete,
    trans.deleteThisImportedGame,
    trans.replayMode,
    trans.slow,
    trans.fast,
    trans.realtimeReplay,
    trans.byCPL,
    // context menu
    trans.promoteVariation,
    trans.makeMainLine,
    trans.deleteFromHere,
    trans.forceVariation,
    // practice (also uses checkmate, draw)
    trans.practiceWithComputer,
    trans.puzzle.goodMove,
    trans.inaccuracy,
    trans.mistake,
    trans.blunder,
    trans.threefoldRepetition,
    trans.anotherWasX,
    trans.bestWasX,
    trans.youBrowsedAway,
    trans.resumePractice,
    trans.whiteWinsGame,
    trans.blackWinsGame,
    trans.theGameIsADraw,
    trans.yourTurn,
    trans.computerThinking,
    trans.seeBestMove,
    trans.hideBestMove,
    trans.getAHint,
    trans.evaluatingYourMove,
    // retrospect (also uses youBrowsedAway, bestWasX, evaluatingYourMove)
    trans.learnFromYourMistakes,
    trans.learnFromThisMistake,
    trans.skipThisMove,
    trans.next,
    trans.xWasPlayed,
    trans.findBetterMoveForWhite,
    trans.findBetterMoveForBlack,
    trans.resumeLearning,
    trans.youCanDoBetter,
    trans.tryAnotherMoveForWhite,
    trans.tryAnotherMoveForBlack,
    trans.solution,
    trans.waitingForAnalysis,
    trans.noMistakesFoundForWhite,
    trans.noMistakesFoundForBlack,
    trans.doneReviewingWhiteMistakes,
    trans.doneReviewingBlackMistakes,
    trans.doItAgain,
    trans.reviewWhiteMistakes,
    trans.reviewBlackMistakes,
    // explorer (also uses gameOver, checkmate, stalemate, draw, variantEnding)
    trans.openingExplorerAndTablebase,
    trans.openingExplorer,
    trans.xOpeningExplorer,
    trans.move,
    trans.games,
    trans.variantLoss,
    trans.variantWin,
    trans.insufficientMaterial,
    trans.capture,
    trans.pawnMove,
    trans.close,
    trans.winning,
    trans.unknown,
    trans.losing,
    trans.drawn,
    trans.timeControl,
    trans.averageElo,
    trans.database,
    trans.recentGames,
    trans.topGames,
    trans.whiteDrawBlack,
    trans.averageRatingX,
    trans.masterDbExplanation,
    trans.mateInXHalfMoves,
    trans.nextCaptureOrPawnMoveInXHalfMoves,
    trans.noGameFound,
    trans.maybeIncludeMoreGamesFromThePreferencesMenu,
    trans.winPreventedBy50MoveRule,
    trans.lossSavedBy50MoveRule,
    trans.allSet,
    // advantage and movetime charts
    trans.advantage,
    trans.nbSeconds,
    trans.opening,
    trans.middlegame,
    trans.endgame
  ).map(_.key)
}
