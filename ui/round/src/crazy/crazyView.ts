import { h } from 'snabbdom';
import * as round from '../round';
import { drag, crazyKeys, pieceRoles, pieceShogiRoles } from './crazyCtrl';
import * as cg from 'chessground/types';
import RoundController from '../ctrl';
import { onInsert } from '../util';
import { Position } from '../interfaces';

const eventNames = ['mousedown', 'touchstart'];

export default function pocket(ctrl: RoundController, color: Color, position: Position) {
  const step = round.plyStep(ctrl.data, ctrl.ply);
  const variantKey = ctrl.data.game.variant.key;
  const dropRoles = variantKey == 'crazyhouse' ? pieceRoles : pieceShogiRoles;
  if (!step.crazy) return;
  const droppedRole = ctrl.justDropped,
    preDropRole = ctrl.preDrop,
    pocket = step.crazy.pockets[color === 'white' ? 0 : 1],
    usablePos = position === (ctrl.flip ? 'top' : 'bottom'),
    shogiPlayer = position === 'top' ? 'enemy' : 'ally',
    usable = usablePos && !ctrl.replaying() && ctrl.isPlaying(),
    activeColor = color === ctrl.data.player.color;
  const capturedPiece = ctrl.justCaptured;
  const captured =
    capturedPiece &&
    (variantKey === 'shogi' && capturedPiece['promoted']
      ? (capturedPiece.role.slice(1) as cg.Role)
      : capturedPiece['promoted']
      ? 'p-piece'
      : capturedPiece.role);
  return h(
    'div.pocket.is2d.pocket-' + position,
    {
      class: { usable },
      hook: onInsert(el =>
        eventNames.forEach(name =>
          el.addEventListener(name, (e: cg.MouchEvent) => {
            if (position === (ctrl.flip ? 'top' : 'bottom') && crazyKeys.length == 0) drag(ctrl, e);
          })
        )
      ),
    },
    dropRoles.map(role => {
      let nb = pocket[role] || 0;
      if (activeColor) {
        if (droppedRole === role) nb--;
        if (captured === role) nb++;
      }
      return h(
        'div.pocket-c1',
        h(
          'div.pocket-c2',
          h('piece.' + role + '.' + color + '.' + shogiPlayer, {
            class: { premove: activeColor && preDropRole === role },
            attrs: {
              'data-role': role,
              'data-color': color,
              'data-nb': nb,
            },
          })
        )
      );
    })
  );
}
