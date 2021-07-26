import {Authority} from '../api/api-models';

export abstract class TabModel {

  protected _index: number;
  protected _translateKey: string;
  protected _lastElementPath: string;
  protected _routeCommands: string[];
  protected _requiredAuthorities: Authority[];

  protected abstract get translatePrefix(): string;

  get index(): number {
    return this._index;
  }

  get translateKey(): string {
    return this._translateKey;
  }

  get lastElementPath(): string {
    return this._lastElementPath;
  }

  get routeCommands(): string[] {
    return this._routeCommands;
  }

  get requiredAuthorities(): Authority[] {
    return this._requiredAuthorities;
  }

}
