import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ThemePalette} from '@angular/material/core/common-behaviors/color';
import {MatDialog} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {finalize, takeUntil} from 'rxjs/operators';
import {LegalActDTO, ProjectBaseDTO, ProjectDataForUpsertDTO, ProjectDTO, UserDTO} from '../../../../../common/api/api-models';
import {BaseComponent} from '../../../../../common/components/base.component';
import {RouteProjectManagement} from '../../../../../common/const/routes';
import {DateTimeUtils} from '../../../../../common/utils/date-time.utils';
import {RequiredValidator} from '../../../../../common/validators/required.validator';
import {ProjectService} from '../../../services/project.service';
import {ProjectHelper} from '../project-helper';
import {ManageLegalActsModalComponent} from './manage-legal-acts-modal/manage-legal-acts-modal.component';
import {
  ManageParticipantsModalComponent,
  ManageParticipantsModalModel,
  SelectedParticipantConfirmModel
} from './manage-participants-modal/manage-participants-modal.component';

@Component({
  templateUrl: './project-upsert.component.html',
  styleUrls: ['./project-upsert.component.scss']
})
export class ProjectUpsertComponent extends BaseComponent implements OnInit {

  readonly prefixTranslateMessage: string = 'project-management.projects-upsert.';

  readonly ctrlName: string = 'name';
  readonly ctrlDateStart: string = 'dateStart';
  readonly ctrlDateEnd: string = 'dateEnd';
  readonly ctrlDescription: string = 'description';
  readonly ctrlProjectManager: string = 'projectManager';

  loading: boolean = false;

  dateStart: Date;
  dateEnd: Date;

  selectedParticipantsHasOverlappingProject: boolean = false;
  selectedParticipantProjects: ProjectBaseDTO[] = [];
  selectedParticipants: UserDTO[] = [];

  selectedLegalActs: LegalActDTO[] = [];

  project: ProjectDTO;
  form: FormGroup;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    private projectService: ProjectService
  ) {
    super();
  }

  ngOnInit(): void {
    this.initForm();
    this.activatedRoute.params.subscribe((params: Params) => this.initProjectForUpdate(params));
  }

  clickSave(): void {
    const dto: ProjectDTO = this.buildDTO();
    if (!this.project) {
      this.createProject(dto);
    } else {
      this.updateProject(dto);
    }
  }

  clickManageLegalActs(): void {
    this.dialog.open(ManageLegalActsModalComponent, {data: this.selectedLegalActs})
      .afterClosed()
      .pipe(takeUntil(this.destroy$))
      .subscribe((selectedLegalActs: LegalActDTO[]) => {
        if (selectedLegalActs) {
          this.selectedLegalActs = selectedLegalActs;
        }
      });
  }

  getManageLegalActsButtonColor(): ThemePalette {
    return this.selectedLegalActs.length < 1 ? 'warn' : null;
  }

  clickManageParticipants(): void {
    if (!this.datesAreSet()) {
      return;
    }
    const modalData: ManageParticipantsModalModel = {
      dateStart: this.dateStart,
      dateEnd: this.dateEnd,
      initSelectedParticipants: this.selectedParticipants,
      excludedOverlappingId: this.project ? this.project.id : null
    };
    this.dialog.open(ManageParticipantsModalComponent, {data: modalData})
      .afterClosed()
      .pipe(takeUntil(this.destroy$))
      .subscribe((confirmModel: SelectedParticipantConfirmModel) => this.changeSelectedParticipantsAfterConfirm(confirmModel));
  }

  getManageParticipantButtonTooltip(): string {
    let suffixTranslateMessage: string;
    if (!this.datesAreSet()) {
      suffixTranslateMessage = 'manage-participants-need-date-ranges';
    } else if (this.selectedParticipantsHasOverlappingProject) {
      suffixTranslateMessage = 'manage-participants-has-overlapping-projects';
    } else {
      suffixTranslateMessage = 'manage-participants';
    }
    return this.translateService.instant(this.prefixTranslateMessage + suffixTranslateMessage);
  }

  getManageParticipantButtonColor(): ThemePalette {
    let color: ThemePalette;
    if (!this.datesAreSet() || this.selectedParticipantsHasOverlappingProject) {
      color = undefined;
    } else if (this.selectedParticipants.length < 1) {
      color = 'warn';
    } else {
      color = 'primary';
    }
    return color;
  }

  datesAreSet(): boolean {
    return !(!this.dateStart || !this.dateEnd);
  }

  dateChanged(): void {
    this.selectedParticipantsHasOverlappingProject = this.projectsHasOverlappingDates(this.selectedParticipantProjects);
  }

  private initForm(): void {
    this.form = this.formBuilder.group({
      [this.ctrlName]: [null, RequiredValidator.requiredTrim],
      [this.ctrlDateStart]: [null, RequiredValidator.required],
      [this.ctrlDateEnd]: [null, RequiredValidator.required],
      [this.ctrlProjectManager]: [null, RequiredValidator.required],
      [this.ctrlDescription]: [null, RequiredValidator.requiredTrim]
    });
  }

  private buildDTO(): ProjectDTO {
    return {
      name: this.form.controls[this.ctrlName].value,
      dateStartStr: DateTimeUtils.dateToString(this.dateStart),
      dateEndStr: DateTimeUtils.dateToString(this.dateEnd),
      participants: this.selectedParticipants,
      projectManager: this.form.controls[this.ctrlProjectManager].value,
      legalActs: this.selectedLegalActs,
      description: this.form.controls[this.ctrlDescription].value
    };
  }

  private createProject(dto: ProjectDTO): void {
    this.projectService.create(dto)
      .pipe(finalize(() => this.loading = false))
      .subscribe((createdProject: ProjectDTO) => {
        if (createdProject) {
          this.afterUpsertData(createdProject, 'common.saved');
          this.router.navigate([createdProject.id], {
            relativeTo: this.activatedRoute,
            queryParamsHandling: 'merge'
          });
        }
      });
  }

  private updateProject(dto: ProjectDTO): void {
    dto.id = this.project.id;
    dto.entityVersion = this.project.entityVersion;
    this.projectService.update(dto)
      .pipe(finalize(() => this.loading = false))
      .subscribe((updatedProject: ProjectDTO) => {
        if (updatedProject) {
          this.afterUpsertData(updatedProject, 'common.updated');
        }
      });
  }

  private afterUpsertData(changedDTO: ProjectDTO, translationKeyForSuccess: string): void {
    this.project = changedDTO;
    const translateMessage: string = this.translateService.instant(translationKeyForSuccess);
    this.snackBar.open(translateMessage, 'OK', {duration: 2000});
  }

  private initProjectForUpdate(params: Params): void {
    if (!params) {
      return;
    }
    const projectId: number = +params[RouteProjectManagement.PROJECTS_UPSERT_ID_PARAM];
    if (!projectId) {
      return;
    }
    if (this.project && this.project.id === projectId) {
      return;
    }
    this.loadProjectFromServer(projectId);
  }

  private loadProjectFromServer(projectId: number): void {
    this.loading = true;
    this.projectService.findForUpsertById(projectId)
      .pipe(finalize(() => this.loading = false))
      .subscribe((projectDataForUpsert: ProjectDataForUpsertDTO) => this.changeDateAfterLoadFromServer(projectDataForUpsert));
  }

  private changeDateAfterLoadFromServer(projectDataForUpsert: ProjectDataForUpsertDTO): void {
    const project: ProjectDTO = projectDataForUpsert.project;
    this.project = project;
    this.form.patchValue(project);
    this.dateStart = DateTimeUtils.stringToDate(project.dateStartStr);
    this.dateEnd = DateTimeUtils.stringToDate(project.dateEndStr);
    this.selectedLegalActs = projectDataForUpsert.project.legalActs;
    this.changeSelectedParticipantAfterLoadFromServer(projectDataForUpsert);
  }

  private changeSelectedParticipantAfterLoadFromServer(projectDataForUpsert: ProjectDataForUpsertDTO): void {
    this.selectedParticipants = projectDataForUpsert.project.participants || [];
    this.sortSelectedParticipants();
    this.selectedParticipantProjects = projectDataForUpsert.participantsProjects || [];
    this.selectedParticipantsHasOverlappingProject = this.projectsHasOverlappingDates(this.selectedParticipantProjects);
    const projectManager: UserDTO = this.selectedParticipants.find(p => p.id === projectDataForUpsert.project.projectManager.id);
    this.form.controls[this.ctrlProjectManager].setValue(projectManager);
  }

  private changeSelectedParticipantsAfterConfirm(confirmModel: SelectedParticipantConfirmModel): void {
    if (!confirmModel) {
      return;
    }
    this.selectedParticipantsHasOverlappingProject = this.projectsHasOverlappingDates(confirmModel.projects);
    this.selectedParticipantProjects = confirmModel.projects;
    this.selectedParticipants = confirmModel.selectedParticipants;
    this.sortSelectedParticipants();
  }

  private sortSelectedParticipants(): void {
    if (this.selectedParticipants) {
      this.selectedParticipants.sort((a, b) => ProjectHelper.compareUser(a, b));
    }
  }

  private projectsHasOverlappingDates(projects: ProjectBaseDTO[]): boolean {
    if (!this.dateStart || !this.dateEnd || !projects) {
      return false;
    }
    let hasOverlappingDates: boolean = false;
    const excludedIs: number = this.project ? this.project.id : null;
    for (let project of projects) {
      if (ProjectHelper.projectHasOverlappingDates(project, this.dateStart, this.dateEnd, excludedIs)) {
        hasOverlappingDates = true;
        break;
      }
    }
    return hasOverlappingDates;
  }

}
