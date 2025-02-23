import { BaseGameData, ContinueMode } from './interfaces';

export function game(data: BaseGameData, color?: Color, embed?: boolean): string;
export function game(data: string, color?: Color, embed?: boolean): string;
export function game(data: any, color?: Color, embed?: boolean): string {
  const id = data.game ? data.game.id : data;
  return (embed ? '/embed/' : '/') + id + (color ? '/' + color : '');
}

export function cont(data: BaseGameData, mode: ContinueMode): string {
  return game(data) + '/continue/' + mode;
}
