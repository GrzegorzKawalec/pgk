import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {Type} from '@angular/core';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslateModule} from '@ngx-translate/core';
import {PgkPipesModule} from '../pipes/pgk-pipes.module';

export const COMMON_MODULES: Array<Type<any> | any[]> = [
  CommonModule,
  HttpClientModule,
  FlexLayoutModule,
  FormsModule,
  ReactiveFormsModule,
  TranslateModule,

  PgkPipesModule
];
