import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {Provider} from '@angular/core';
import {ExceptionInterceptor} from './exception/exception.interceptor';

export const INTERCEPTORS: Provider[] = [
  {provide: HTTP_INTERCEPTORS, useClass: ExceptionInterceptor, multi: true}
];
