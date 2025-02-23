const variantConfirms = {
  chess960: "This is a Chess960 game!\n\nThe starting position of the pieces on the players' home ranks is randomized.",
  kingOfTheHill: 'This is a King of the Hill game!\n\nThe game can be won by bringing the king to the center.',
  threeCheck: 'This is a Three-check game!\n\nThe game can be won by checking the opponent three times.',
  fiveCheck: 'This is a Five-check game!\n\nThe game can be won by checking the opponent five times.',
  antichess:
    'This is an Antichess game!\n\nIf you can take a piece, you must. The game can be won by losing all your pieces, or by being stalemated.',
  atomic:
    "This is an Atomic chess game!\n\nCapturing a piece causes an explosion, taking out your piece and surrounding non-pawns. Win by mating or exploding your opponent's king.",
  horde: 'This is a Horde chess game!\nBlack must take all White pawns to win. White must checkmate the Black king.',
  racingKings:
    'This is a Racing Kings game!\n\nPlayers must race their kings to the eighth rank. Checks are not allowed.',
  crazyhouse:
    'This is a Crazyhouse game!\n\nEvery time a piece is captured, the capturing player gets a piece of the same type and of their color in their pocket.',
  linesOfAction:
    "This is Lines Of Action, a separate game from chess. The aim of the game is to connect all of one's pieces, with movement variable on the number of pieces in a line.",
  frisian: 'This is a Frisian draughts game!\n\nPieces can also capture horizontally and vertically.',
  frysk: 'This is a Frysk! game!\n\nFrisian draughts starting with 5 pieces each.',
  antidraughts:
    'This is an Antidraughts game!\n\nThe game can be won by losing all your pieces, or running out of moves.',
  breakthrough: 'This is a Breakthrough game!\n\nThe first player who makes a king wins.',
};

function storageKey(key) {
  return 'lobby.variant.' + key;
}

export default function (variant: string) {
  return Object.keys(variantConfirms).every(function (key) {
    const v = variantConfirms[key];
    if (variant === key && !playstrategy.storage.get(storageKey(key))) {
      const c = confirm(v);
      if (c) playstrategy.storage.set(storageKey(key), '1');
      return c;
    } else return true;
  });
}
