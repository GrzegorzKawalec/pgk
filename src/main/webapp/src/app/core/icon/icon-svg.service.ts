import {Injectable} from '@angular/core';
import {MatIconRegistry} from '@angular/material/icon';
import {DomSanitizer} from '@angular/platform-browser';
import {IconSvgModel} from './icon-svg.model';
import {REGISTERED_ICONS} from './registered-icons';

@Injectable({
  providedIn: 'root'
})
export class IconSvgService {

  private isRegistered: boolean = false;

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {
  }

  registerIcons(): void {
    if (this.isRegistered || REGISTERED_ICONS.length < 1) {
      return;
    }
    REGISTERED_ICONS.forEach(icon => this.registerSingleIcon(icon));
    this.isRegistered = true;
  }

  private registerSingleIcon(icon: IconSvgModel): void {
    this.matIconRegistry.addSvgIcon(
      icon.name,
      this.domSanitizer.bypassSecurityTrustResourceUrl(icon.path)
    );
  }

}
