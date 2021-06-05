import {Component, Inject} from '@angular/core';
import {ThemePalette} from '@angular/material/core/common-behaviors/color';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {TranslateService} from '@ngx-translate/core';
import {ModalConfirmModel} from './modal-confirm.model';

@Component({
  templateUrl: './modal-confirm.component.html',
  styleUrls: ['./modal-confirm.component.scss']
})
export class ModalConfirmComponent {

  title: string;
  content: string;
  defaultContent: string;

  cancelButtonLabel: string;
  cancelButtonColor: ThemePalette;

  confirmButtonLabel: string;
  confirmButtonColor: ThemePalette;

  constructor(
    @Inject(MAT_DIALOG_DATA) data: ModalConfirmModel,
    private dialogRef: MatDialogRef<ModalConfirmComponent>,
    private translateService: TranslateService
  ) {
    const model: ModalConfirmModel = ModalConfirmComponent.initModel(data);
    this.initFields(model);
  }

  private static initModel(initModel: ModalConfirmModel): ModalConfirmModel {
    const model: ModalConfirmModel = ModalConfirmComponent.getDefaultModel();
    if (!initModel) {
      return model;
    }

    if (initModel.showTitle === true || initModel.showTitle === false) {
      model.showTitle = initModel.showTitle;
    }
    if (initModel.title) {
      model.title = initModel.title;
    }
    if (initModel.titleTranslateKey) {
      model.titleTranslateKey = initModel.titleTranslateKey;
    }

    if (initModel.showDefaultContent === true || initModel.showDefaultContent === false) {
      model.showDefaultContent = initModel.showDefaultContent;
    }
    if (initModel.content) {
      model.content = initModel.content;
    }
    if (initModel.contentTranslateKey) {
      model.contentTranslateKey = initModel.contentTranslateKey;
    }

    if (initModel.cancelButtonLabel) {
      model.cancelButtonLabel = initModel.cancelButtonLabel;
    }
    if (initModel.cancelButtonLabelTranslateKey) {
      model.cancelButtonLabelTranslateKey = initModel.cancelButtonLabelTranslateKey;
    }
    if (initModel.cancelButtonColor) {
      model.cancelButtonColor = initModel.confirmButtonColor;
    }


    if (initModel.confirmButtonLabel) {
      model.confirmButtonLabel = initModel.confirmButtonLabel;
    }
    if (initModel.confirmButtonLabelTranslateKey) {
      model.confirmButtonLabelTranslateKey = initModel.confirmButtonLabelTranslateKey;
    }
    if (initModel.confirmButtonColor) {
      model.confirmButtonColor = initModel.confirmButtonColor;
    }

    return model;
  }

  private initFields(model: ModalConfirmModel): void {
    if (model.showTitle) {
      this.title = model.title ? model.title : this.translate(model.titleTranslateKey);
    }
    if (model.showDefaultContent) {
      const defaultContentTranslateKey: string = ModalConfirmComponent.getDefaultContentTranslateKey();
      this.defaultContent = this.translateService.instant(defaultContentTranslateKey);
    }
    this.content = model.content ? model.content : this.translate(model.contentTranslateKey);
    if (this.content === this.defaultContent) {
      this.defaultContent = null;
    }
    this.cancelButtonLabel = model.cancelButtonLabel ? model.cancelButtonLabel : this.translate(model.cancelButtonLabelTranslateKey);
    this.confirmButtonLabel = model.confirmButtonLabel ? model.confirmButtonLabel : this.translate(model.confirmButtonLabelTranslateKey);
    this.cancelButtonColor = model.cancelButtonColor;
    this.confirmButtonColor = model.confirmButtonColor;
  }

  private translate(translateKey: string): string {
    const translate: string = !translateKey ? '' :
      this.translateService.instant(translateKey);
    return translate === translateKey ? null : translate;
  }

  private static getDefaultModel(): ModalConfirmModel {
    return {
      showTitle: true,
      showDefaultContent: false,
      titleTranslateKey: 'common.are-you-sure',
      contentTranslateKey: ModalConfirmComponent.getDefaultContentTranslateKey(),
      cancelButtonLabelTranslateKey: 'common.cancel',
      confirmButtonLabelTranslateKey: 'common.confirm',
      cancelButtonColor: undefined,
      confirmButtonColor: 'accent'
    };
  }

  private static getDefaultContentTranslateKey(): string {
    return 'common.operation-cannot-be-undone';
  }

}
