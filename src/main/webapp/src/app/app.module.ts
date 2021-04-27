import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

import {AppComponent} from './app.component';
import {AppRouting} from './app.routing';
import {COMMON_MODULES} from './core/const/common-modules';
import {NavbarComponent} from './layouts/navbar/navbar.component';
import {AppInfoModule} from './modules/app-info/app-info.module';

export function createTranslateLoader(http: HttpClient): TranslateHttpLoader {
  const timestamp = Date.now();
  return new TranslateHttpLoader(http, './assets/translations/', '.json?t=' + timestamp);
}

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    COMMON_MODULES,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    }),

    AppRouting,
    AppInfoModule,

    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatTooltipModule
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
