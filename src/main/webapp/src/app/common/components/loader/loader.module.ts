import {NgModule} from '@angular/core';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {COMMON_MODULES} from '../../const/common-modules';
import {LoaderComponent} from './loader.component';

@NgModule({
  declarations: [LoaderComponent],
  exports: [LoaderComponent],
  imports: [
    COMMON_MODULES,
    MatProgressSpinnerModule
  ]
})
export class LoaderModule {
}
