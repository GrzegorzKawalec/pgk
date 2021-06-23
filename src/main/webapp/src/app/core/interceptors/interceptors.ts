import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {Provider} from '@angular/core';
import {AuthInterceptor} from './auth/auth-interceptor';
import {ExceptionInterceptor} from './exception/exception.interceptor';

export const INTERCEPTORS: Provider[] = [

  {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: ExceptionInterceptor, multi: true}

];
