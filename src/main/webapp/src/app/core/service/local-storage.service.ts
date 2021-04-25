import {Injectable} from '@angular/core';
import {LocalStorageKey} from '../const/local-storage-key';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  read<T>(key: LocalStorageKey): T {
    const lsKey: string = Keys.get(key);
    if (!lsKey) {
      return null;
    }
    const rawData: string = localStorage.getItem(lsKey);
    return rawData ? JSON.parse(rawData) : null;
  }

  write<T>(key: LocalStorageKey, data: T): void {
    const lsKey: string = Keys.get(key);
    if (!data || !lsKey) {
      return;
    }
    const forSave: string = JSON.stringify(data);
    localStorage.setItem(lsKey, forSave);
  }

}

class Keys {

  private static readonly PREFIX: string = 'pgk-';

  static get(key: LocalStorageKey): string {
    let lsKey;
    switch (key) {
      case LocalStorageKey.LS_LANG:
        lsKey = 'lang';
        break;
      default:
        return null;
    }
    return this.PREFIX + lsKey;
  }

}
