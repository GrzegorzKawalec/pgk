import {Component, Inject, ViewChild} from '@angular/core';
import {MatCheckboxChange} from '@angular/material/checkbox';
import {ThemePalette} from '@angular/material/core/common-behaviors/color';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {MatListOption, MatSelectionList, MatSelectionListChange} from '@angular/material/list/selection-list';
import {TranslateService} from '@ngx-translate/core';
import {Subject} from 'rxjs';
import {finalize} from 'rxjs/operators';
import {LegalActDTO} from '../../../../../../common/api/api-models';
import {StringUtils} from '../../../../../../common/utils/string.utils';
import {LegalActService} from '../../../../services/legal-act.service';

@Component({
  templateUrl: './manage-legal-acts-modal.component.html',
  styleUrls: ['./manage-legal-acts-modal.component.scss']
})
export class ManageLegalActsModalComponent {

  readonly prefixTranslateMessage: string = 'project-management.projects-upsert.';

  allLegalActs: LegalActModel[] = [];
  legalActsForView: LegalActModel[] = [];

  matSelected: MatListOption[] = [];
  selectedLegalActs: LegalActModel[] = [];
  @ViewChild('legalActList') legalActList: MatSelectionList;

  loading: boolean = false;

  filterOnlySelectedLegalActs: boolean = false;
  filterOnlyActiveLegalActs: boolean = true;

  filterText: string;
  private applyTextFilter$: Subject<string> = new Subject();

  constructor(
    @Inject(MAT_DIALOG_DATA) data: LegalActDTO[],
    private dialogRef: MatDialogRef<ManageLegalActsModalComponent>,
    private translateService: TranslateService,
    private legalActService: LegalActService
  ) {
    this.loadLegalActs(data);
  }

  clickConfirm(): void {
    this.dialogRef.close(this.selectedLegalActs || []);
  }

  clickUnselect(): void {
    this.legalActList.deselectAll();
    if (this.filterOnlySelectedLegalActs) {
      this.filterLegalActs();
    }
  }

  clickLink(legalAct: LegalActModel): void {
    this.restorePreviousSelectionState(legalAct);
    if (legalAct.active) {
      window.open(legalAct.link, '_blank');
    }
  }

  applyTextFilter(event: KeyboardEvent): void {
    let filterValue: string = null;
    if (event && event.target) {
      filterValue = (event.target as HTMLInputElement).value;
    }
    this.filterText = filterValue;
    this.applyTextFilter$.next(filterValue);
  }

  applyOnlyActiveLegalActsFilter(change: MatCheckboxChange): void {
    this.filterOnlyActiveLegalActs = change.checked;
    this.filterLegalActs();
  }

  applyOnlySelectedLegalActsFilter(change: MatCheckboxChange): void {
    this.filterOnlySelectedLegalActs = change.checked;
    this.filterLegalActs();
  }

  listSelectionChanged(changedSelectionElement: MatSelectionListChange) {
    const clickedElement: MatListOption = changedSelectionElement.options[0];
    const legalAct: LegalActModel = clickedElement.value;
    if (!this.findMatSelectElement(legalAct)) {
      this.matSelected.push(clickedElement);
    }
    if (this.filterOnlySelectedLegalActs && !clickedElement.selected) {
      this.filterLegalActs();
    }
  }

  getTooltipForParticipant(legalAct: LegalActModel): string {
    const suffix: string = legalAct.active ? '' :
      '(' + this.translateService.instant(this.prefixTranslateMessage + 'inactive') + ')';
    return this.translateService.instant(this.prefixTranslateMessage + 'date-of') + ': ' + legalAct.dateOfStr + suffix;
  }

  getColorConfirmButton(): ThemePalette {
    return this.selectedLegalActs.length < 1 ? 'warn' : 'primary';
  }

  private restorePreviousSelectionState(legalAct: LegalActModel): void {
    setTimeout(() => {
      const matSelectElement: MatListOption = this.findMatSelectElement(legalAct);
      matSelectElement.selected = !matSelectElement.selected;
    });
  }

  private findMatSelectElement(legalAct: LegalActModel): MatListOption {
    return this.matSelected.find(m => m.value === legalAct);
  }

  private loadLegalActs(initialSelected: LegalActDTO[]): void {
    this.loading = true;
    this.legalActService.getAll()
      .pipe(finalize(() => this.loading = false))
      .subscribe((legalActs: LegalActDTO[]) => this.prepareModels(legalActs, initialSelected));
  }

  private prepareModels(legalActs: LegalActDTO[], initialSelected: LegalActDTO[]): void {
    if (!legalActs || legalActs.length < 1) {
      this.allLegalActs = [];
      this.legalActsForView = [];
      return;
    }
    initialSelected = initialSelected || [];
    legalActs.forEach(legalAct => {
      const model: LegalActModel = this.prepareSingleModel(legalAct, initialSelected);
      this.allLegalActs.push(model);
      this.legalActsForView.push(model);
    });
    this.filterLegalActs();
    this.sortLegalActs();
  }

  private prepareSingleModel(legalAct: LegalActDTO, initialSelected: LegalActDTO[]): LegalActModel {
    return {
      ...legalAct,
      initialSelected: !!initialSelected.find(l => l.id === legalAct.id)
    };
  }

  private sortLegalActs(): void {
    if (!this.legalActsForView || this.legalActsForView.length < 1) {
      return;
    }
    this.legalActsForView.sort((a, b) => StringUtils.compareString(a.name, b.name));
  }

  private filterLegalActs(): void {
    if (!this.allLegalActs) {
      this.legalActsForView = [];
      return;
    }
    const result: LegalActModel[] = [];
    const arrayForFilter: LegalActModel[] = this.filterOnlySelectedLegalActs ? this.selectedLegalActs : this.allLegalActs;
    for (let legalAct of arrayForFilter) {
      if (this.filterOnlyActiveLegalActs && !legalAct.active) {
        continue;
      }
      if (this.containNameOrDescription(legalAct)) {
        result.push(legalAct);
      }
    }
    this.legalActsForView = result;
  }

  private containNameOrDescription(legalAct: LegalActModel): boolean {
    if (!this.filterText) {
      return true;
    }
    return this.containFilterText(legalAct.name) || this.containFilterText(legalAct.description);
  }

  private containFilterText(checkedText: string): boolean {
    return StringUtils.containIgnoreCase(checkedText, this.filterText);
  }

}

interface LegalActModel extends LegalActDTO {
  initialSelected: boolean;
}
