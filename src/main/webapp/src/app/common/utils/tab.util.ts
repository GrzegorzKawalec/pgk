import {MatTabChangeEvent} from '@angular/material/tabs/tab-group';
import {Router} from '@angular/router';
import {AuthHelper} from '../../core/auth/auth.helper';
import {TabModel} from '../models/tab.model';

export class TabUtil {

  static filterAndSortTabs<T extends TabModel>(tabs: T[], authHelper: AuthHelper): T[] {
    if (!tabs) {
      return [];
    }
    const filterTabs: T[] = tabs.filter(t => authHelper.hasAuthorities(t.requiredAuthorities)) || [];
    return filterTabs.sort((t1, t2) => t1.index - t2.index);
  }

  static getActiveTabIndexFromRouter<T extends TabModel>(tabs: T[], router: Router): number {
    const urlComponents: string[] = router.url.split('/');
    const lastIndex: number = urlComponents ? urlComponents.length - 1 : null;
    const lastElementPath: string = lastIndex ? urlComponents[lastIndex] : null;
    const tab: T = lastElementPath ? tabs.find(tab => tab.lastElementPath === lastElementPath) : null;
    return tab ? tab.index : 0;
  }

  static onTabChange<T extends TabModel>(tabs: T[], router: Router, changeEvent: MatTabChangeEvent): void {
    const tab: T = tabs.find(tab => tab.index === changeEvent.index);
    if (tab) {
      const url: string = '/' + tab.routeCommands.join('/');
      router.navigateByUrl(url);
    }
  }

}
