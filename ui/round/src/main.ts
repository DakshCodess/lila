import { attributesModule, classModule, init } from 'snabbdom';
import boot from './boot';
import PlayStrategyChat from 'chat';
import menuHover from 'common/menuHover';
import MoveOn from './moveOn';
import RoundController from './ctrl';
import { Draughtsground } from 'draughtsground';
import { Chessground } from 'chessground';
import { main as view } from './view/main';
import { RoundOpts } from './interfaces';

export interface RoundApi {
  socketReceive(typ: string, data: any): boolean;
  moveOn: MoveOn;
}

export interface RoundMain {
  app: (opts: RoundOpts) => RoundApi;
}

const patch = init([classModule, attributesModule]);

export function app(opts: RoundOpts): RoundApi {
  const ctrl = new RoundController(opts, redraw);

  const blueprint = view(ctrl);
  opts.element.innerHTML = '';
  let vnode = patch(opts.element, blueprint);

  function redraw() {
    vnode = patch(vnode, view(ctrl));
  }

  window.addEventListener('resize', redraw); // col1 / col2+ transition

  if (ctrl.isPlaying()) menuHover();

  return {
    socketReceive: ctrl.socket.receive,
    moveOn: ctrl.moveOn,
  };
}

export { boot };

window.PlayStrategyChat = PlayStrategyChat;
// that's for the rest of playstrategy to access chessground
// without having to include it a second time
window.Chessground = Chessground;
window.Draughtsground = Draughtsground; // We need both for the "ongoing games" underneath the current one.
