import {registerLocaleData} from '@angular/common';

import localePL from '@angular/common/locales/pl';
import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DEFAULT_LANG} from './common/const/langs';
import {LocalStorageKey} from './common/services/local-storage/local-storage-key';
import {IconSvgService} from './core/icon/icon-svg.service';
import {LocalStorageService} from './common/services/local-storage/local-storage.service';

@Component({
  selector: 'pgk-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(
    private translate: TranslateService,
    private iconSvgService: IconSvgService,
    private localStorageService: LocalStorageService
  ) {
    this.initLang();
    this.iconSvgService.registerIcons();
  }

  private initLang(): void {
    this.translate.setDefaultLang(DEFAULT_LANG);
    this.translate.use(this.getCurrentLang() || document.documentElement.lang).subscribe();
    document.documentElement.lang = this.translate.currentLang;
    registerLocaleData(localePL);
  }

  private getCurrentLang(): string {
    const lastLang: string = this.localStorageService.read(LocalStorageKey.LS_LANG);
    return lastLang ? lastLang : DEFAULT_LANG;
  }

}
