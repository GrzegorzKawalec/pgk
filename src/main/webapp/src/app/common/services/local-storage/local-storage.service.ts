import {Injectable} from '@angular/core';
import {LocalStorageKey} from './local-storage-key';

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
    if (!lsKey) {
      return;
    }
    const forSave: string = JSON.stringify(data);
    localStorage.setItem(lsKey, forSave);
  }

}

class Keys {

  private static readonly PREFIX: string = 'pgk-';

  static get(key: LocalStorageKey): string {
    let lsKey: string;
    switch (key) {
      case LocalStorageKey.LANG:
        lsKey = 'lang';
        break;
      case LocalStorageKey.LEGAL_ACTS_PER_PAGE:
        lsKey = 'legal-acts-per-page';
        break;
      case LocalStorageKey.PROJECTS_PER_PAGE:
        lsKey = 'projects-per-page';
        break;
      case LocalStorageKey.ROLES_ITEM_PER_PAGE:
        lsKey = 'roles-item-per-page';
        break;
      case LocalStorageKey.USERS_ITEM_PER_PAGE:
        lsKey = 'users-item-per-page';
        break;
      default:
        return null;
    }
    return this.PREFIX + lsKey;
  }

}
