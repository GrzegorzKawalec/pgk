import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

import {AppComponent} from './app.component';
import {AppRouting} from './app.routing';
import {CenterCardModule} from './common/components/center-card/center-card.module';
import {COMMON_MODULES} from './common/const/common-modules';
import {SignInComponent} from './core/auth/sign-in/sign-in.component';
import {ExceptionInfoBodyComponent} from './core/interceptors/exception/exception-info-body.component';
import {INTERCEPTORS} from './core/interceptors/interceptors';
import {NavbarComponent} from './layouts/navbar/navbar.component';
import {AppInfoModule} from './modules/app-info/app-info.module';
import {UserManagementModule} from './modules/user-management/user-management.module';

export function createTranslateLoader(http: HttpClient): TranslateHttpLoader {
  const timestamp: number = Date.now();
  return new TranslateHttpLoader(http, './assets/translations/', '.json?t=' + timestamp);
}

@NgModule({
  declarations: [
    AppComponent,
    ExceptionInfoBodyComponent,
    NavbarComponent,
    SignInComponent
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
    UserManagementModule,

    CenterCardModule,

    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatTooltipModule
  ],
  providers: [
    INTERCEPTORS,
    {provide: LocationStrategy, useClass: HashLocationStrategy}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
