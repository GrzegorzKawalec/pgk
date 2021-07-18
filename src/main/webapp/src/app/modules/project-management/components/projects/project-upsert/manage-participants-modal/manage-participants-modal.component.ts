import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MatCheckboxChange} from '@angular/material/checkbox';
import {ThemePalette} from '@angular/material/core/common-behaviors/color';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {MatListOption, MatSelectionList, MatSelectionListChange} from '@angular/material/list/selection-list';
import {TranslateService} from '@ngx-translate/core';
import {Subject} from 'rxjs';
import {debounceTime, finalize, takeUntil} from 'rxjs/operators';
import {ParticipantDTO, ProjectBaseDTO, UserDTO} from '../../../../../../common/api/api-models';
import {BaseComponent} from '../../../../../../common/components/base.component';
import {StringUtils} from '../../../../../../common/utils/string.utils';
import {ProjectService} from '../../../../services/project.service';
import {ProjectHelper} from '../../project-helper';
import {OverlappingProjectModalComponent} from './overlapping-project-modal/overlapping-project-modal.component';

@Component({
  templateUrl: './manage-participants-modal.component.html',
  styleUrls: ['./manage-participants-modal.component.scss']
})
export class ManageParticipantsModalComponent extends BaseComponent implements OnInit {

  readonly prefixTranslateMessage: string = 'project-management.projects-upsert.';

  readonly dateStart: Date;
  readonly dateEnd: Date;
  readonly excludedOverlappingId: number;

  allParticipants: ParticipantModel[] = [];
  participantsForView: ParticipantModel[] = [];

  matSelected: MatListOption[] = [];
  selectedParticipants: ParticipantModel[] = [];
  @ViewChild('participantList') participantList: MatSelectionList;

  loading: boolean = false;

  filterOnlyNonOverlappingProjects: boolean = true;
  filterOnlySelectedParticipants: boolean = false;
  filterOnlyActiveParticipants: boolean = true;

  filterText: string;
  private applyTextFilter$: Subject<string> = new Subject();

  constructor(
    @Inject(MAT_DIALOG_DATA) data: ManageParticipantsModalModel,
    private dialogRef: MatDialogRef<ManageParticipantsModalComponent>,
    private translateService: TranslateService,
    private projectService: ProjectService,
    private dialog: MatDialog
  ) {
    super();
    this.dateStart = data.dateStart;
    this.dateEnd = data.dateEnd;
    this.excludedOverlappingId = data.excludedOverlappingId;
    this.loadParticipants(data.initSelectedParticipants);
  }

  ngOnInit(): void {
    this.subscribeTextFilter();
  }

  clickConfirm(): void {
    const selectedParticipants: UserDTO[] = [];
    const projectsParticipants: ProjectBaseDTO[] = [];
    if (this.selectedParticipants) {
      for (let selectedParticipant of this.selectedParticipants) {
        selectedParticipants.push(selectedParticipant.user);
        if (selectedParticipant.projects) {
          for (let project of selectedParticipant.projects) {
            if (!projectsParticipants.find(p => p.id === project.id)) {
              projectsParticipants.push(project);
            }
          }
        }
      }
    }
    const confirmModel: SelectedParticipantConfirmModel = {
      selectedParticipants: selectedParticipants,
      projects: projectsParticipants
    };
    this.dialogRef.close(confirmModel);
  }

  listSelectionChanged(changedSelectionElement: MatSelectionListChange) {
    const clickedElement: MatListOption = changedSelectionElement.options[0];
    const participant: ParticipantModel = clickedElement.value;
    if (!this.findMatSelectElement(participant)) {
      this.matSelected.push(clickedElement);
    }
    if (this.filterOnlySelectedParticipants && !clickedElement.selected) {
      this.filterParticipantView();
    }
  }

  getTooltipForParticipant(participant: ParticipantModel): string {
    const suffix: string = participant.user.active ? '' :
      '(' + this.translateService.instant(this.prefixTranslateMessage + 'inactive') + ')';
    return participant.user.email + ' ' + suffix;
  }

  getColorConfirmButton(): ThemePalette {
    return this.selectedParticipants.length < 1 ? 'warn' : 'primary';
  }

  clickUnselect(): void {
    this.participantList.deselectAll();
    if (this.filterOnlySelectedParticipants) {
      this.filterParticipantView();
    }
  }

  clickShowOverlappingProjects(participant: ParticipantModel): void {
    if (participant.hasOverlappingDates) {
      this.restorePreviousSelectionState(participant);
      this.dialog.open(OverlappingProjectModalComponent, {data: participant});
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

  applyOnlyNonOverlappingProjectsFilter(change: MatCheckboxChange) {
    this.filterOnlyNonOverlappingProjects = change.checked;
    this.filterParticipantView();
  }

  applyOnlyActiveParticipantsFilter(change: MatCheckboxChange) {
    this.filterOnlyActiveParticipants = change.checked;
    this.filterParticipantView();
  }

  applyOnlySelectedParticipantsFilter(change: MatCheckboxChange) {
    this.filterOnlySelectedParticipants = change.checked;
    this.filterParticipantView();
  }

  private restorePreviousSelectionState(participant: ParticipantModel): void {
    setTimeout(() => {
      const matSelectElement: MatListOption = this.findMatSelectElement(participant);
      matSelectElement.selected = !matSelectElement.selected;
    });
  }

  private findMatSelectElement(participant: ParticipantModel): MatListOption {
    return this.matSelected.find(m => m.value === participant);
  }

  private loadParticipants(initSelectedParticipants: UserDTO[]): void {
    this.loading = true;
    this.projectService.getParticipants()
      .pipe(finalize(() => this.loading = false))
      .subscribe((participants: ParticipantDTO[]) => this.prepareModels(participants, initSelectedParticipants));
  }

  private prepareModels(participants: ParticipantDTO[], initSelectedParticipants: UserDTO[]): void {
    if (!participants || participants.length < 1) {
      this.allParticipants = [];
      this.participantsForView = [];
      return;
    }
    initSelectedParticipants = initSelectedParticipants || [];
    participants.forEach(participant => {
      const model: ParticipantModel = this.prepareSingleModel(participant, initSelectedParticipants);
      this.allParticipants.push(model);
      this.participantsForView.push(model);
    });
    this.filterParticipantView();
    this.sortParticipantView();
  }

  private prepareSingleModel(participant: ParticipantDTO, initSelectedParticipants: UserDTO[]): ParticipantModel {
    const participantModel: ParticipantModel = {
      ...participant,
      initialSelected: !!initSelectedParticipants.find(user => user.id === participant.user.id),
      hasOverlappingDates: false,
      projects: []
    };
    this.setOverlappingDates(participant, participantModel);
    return participantModel;
  }

  private setOverlappingDates(participant: ParticipantDTO, participantModel: ParticipantModel): void {
    if (!participant.projects || participant.projects.length < 1) {
      return;
    }
    const projects: ProjectBaseDTO[] = participant.projects;
    for (let project of projects) {
      const projectModel: ProjectModel = {
        ...project,
        hasOverlappingDates: this.projectHasOverlappingDates(project)
      };
      if (projectModel.hasOverlappingDates) {
        participantModel.hasOverlappingDates = true;
      }
      participantModel.projects.push(projectModel);
    }
  }

  private projectHasOverlappingDates(project: ProjectBaseDTO): boolean {
    return ProjectHelper.projectHasOverlappingDates(project, this.dateStart, this.dateEnd, this.excludedOverlappingId);
  }

  private sortParticipantView(): void {
    if (!this.participantsForView || this.participantsForView.length < 1) {
      return;
    }
    this.participantsForView.sort((a, b) => ProjectHelper.compareUser(a.user, b.user));
  }

  private subscribeTextFilter(): void {
    this.applyTextFilter$
      .pipe(
        takeUntil(this.destroy$),
        debounceTime(300)
      ).subscribe(() => this.filterParticipantView());
  }

  private filterParticipantView(): void {
    if (!this.allParticipants) {
      this.participantsForView = [];
      return;
    }
    const result: ParticipantModel[] = [];
    const arrayForFilter: ParticipantModel[] = this.filterOnlySelectedParticipants ? this.selectedParticipants : this.allParticipants;
    for (let participant of arrayForFilter) {
      if (this.filterOnlyActiveParticipants && !participant.user.active) {
        continue;
      }
      if (this.filterOnlyNonOverlappingProjects && participant.hasOverlappingDates) {
        continue;
      }
      if (this.containParticipantEmailOrName(participant)) {
        result.push(participant);
      }
    }
    this.participantsForView = result;
  }

  private containParticipantEmailOrName(participant: ParticipantModel): boolean {
    if (!this.filterText) {
      return true;
    }
    const user: UserDTO = participant.user;
    return this.containFilterText(user.email) || this.containFilterText(user.lastName) || this.containFilterText(user.firstName);
  }

  private containFilterText(checkedText: string): boolean {
    return StringUtils.containIgnoreCase(checkedText, this.filterText);
  }

}

export interface ManageParticipantsModalModel {
  dateStart: Date,
  dateEnd: Date,
  initSelectedParticipants: UserDTO[]
  excludedOverlappingId: number
}

export interface SelectedParticipantConfirmModel {
  projects: ProjectBaseDTO[];
  selectedParticipants: UserDTO[]
}

export interface ParticipantModel extends ParticipantDTO {
  initialSelected: boolean;
  hasOverlappingDates: boolean;
  projects: ProjectModel[];
}

export interface ProjectModel extends ProjectBaseDTO {
  hasOverlappingDates: boolean;
}
