import {NgModule} from '@angular/core';
import {CheckAccessPipe} from './check-access.pipe';

@NgModule({
  declarations: [
    CheckAccessPipe
  ],
  exports: [
    CheckAccessPipe
  ]
})
export class PgkPipesModule {
}
