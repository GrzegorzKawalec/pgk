import {HttpClient} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {AppRouting} from './app.routing';

import {AppComponent} from './app.component';
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
    AppRouting,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    }),

    AppInfoModule,

    MatButtonModule,
    MatToolbarModule,
    MatTooltipModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
