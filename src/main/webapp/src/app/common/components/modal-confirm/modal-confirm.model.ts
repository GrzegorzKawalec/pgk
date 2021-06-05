import {ThemePalette} from '@angular/material/core/common-behaviors/color';

export interface ModalConfirmModel {
  showTitle?: boolean;
  title?: string,
  titleTranslateKey?: string,

  content?: string;
  contentTranslateKey?: string;
  showDefaultContent?: boolean;

  cancelButtonLabel?: string;
  cancelButtonLabelTranslateKey?: string;
  cancelButtonColor?: ThemePalette;

  confirmButtonLabel?: string;
  confirmButtonLabelTranslateKey?: string;
  confirmButtonColor?: ThemePalette;
}
