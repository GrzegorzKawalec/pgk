import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'pgk-upsert-form',
  templateUrl: './upsert-form.component.html',
  styleUrls: ['./upsert-form.component.scss']
})
export class UpsertFormComponent implements OnChanges {

  @Input() formGroup: FormGroup;

  @Output() clickSaveEvent: EventEmitter<void> = new EventEmitter();

  @Input() loading: boolean;
  @Output() loadingChange: EventEmitter<boolean> = new EventEmitter();

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.hasOwnProperty('loading')) {
      this.changeLoadingState(changes.loading.currentValue);
    }
  }

  clickSave(): void {
    if (this.loading || !this.formGroup.valid) {
      return;
    }
    this.changeLoadingState(true);
    this.clickSaveEvent.emit();
  }

  private changeLoadingState(loading: boolean): void {
    this.loading = loading;
    if (this.loading) {
      this.formGroup.disable();
    } else {
      this.formGroup.enable();
    }
    this.loadingChange.emit(this.loading);
  }

}
