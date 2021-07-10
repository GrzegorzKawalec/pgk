import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {finalize} from 'rxjs/operators';
import {LegalActDTO} from '../../../../../common/api/api-models';
import {RouteProjectManagement} from '../../../../../common/const/routes';
import {DateTimeUtils} from '../../../../../common/utils/date-time.utils';
import {RequiredValidator} from '../../../../../common/validators/required.validator';
import {UrlValidator} from '../../../../../common/validators/url.validator';
import {LegalActService} from '../../../services/legal-act.service';

@Component({
  templateUrl: './legal-acts-upsert.component.html',
  styleUrls: ['./legal-acts-upsert.component.scss']
})
export class LegalActsUpsertComponent implements OnInit {

  readonly prefixTranslateMessage: string = 'project-management.legal-acts-upsert.';

  readonly ctrlName: string = 'name';
  readonly ctrlDateOf: string = 'dateOf';
  readonly ctrlLink: string = 'link';
  readonly ctrlDescription: string = 'description';

  loading: boolean = false;

  legalAct: LegalActDTO;
  form: FormGroup;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    private snackBar: MatSnackBar,
    private legalActService: LegalActService
  ) {
  }

  ngOnInit(): void {
    this.initForm();
    this.activatedRoute.params.subscribe((params: Params) => this.initLegalActForUpdate(params));
  }

  clickSave(): void {
    const dto: LegalActDTO = this.buildDTO();
    if (!this.legalAct) {
      this.createLegalAct(dto);
    } else {
      this.updateLegalAct(dto);
    }
  }

  private initForm(): void {
    this.form = this.formBuilder.group({
      [this.ctrlName]: [null, RequiredValidator.requiredTrim],
      [this.ctrlDateOf]: [null, RequiredValidator.required],
      [this.ctrlLink]: [null, [RequiredValidator.requiredTrim, UrlValidator.url]],
      [this.ctrlDescription]: null
    });
  }

  private buildDTO(): LegalActDTO {
    const dateOf: Date = new Date(this.form.controls[this.ctrlDateOf].value);
    return {
      name: this.form.controls[this.ctrlName].value,
      dateOfStr: DateTimeUtils.dateToString(dateOf),
      link: this.form.controls[this.ctrlLink].value,
      description: this.form.controls[this.ctrlDescription].value
    };
  }

  private createLegalAct(dto: LegalActDTO): void {
    this.legalActService.create(dto)
      .pipe(finalize(() => this.loading = false))
      .subscribe((createdLegalAct: LegalActDTO) => {
        if (createdLegalAct) {
          this.afterUpsertData(createdLegalAct, 'common.saved');
          this.router.navigate([createdLegalAct.id], {
            relativeTo: this.activatedRoute,
            queryParamsHandling: 'merge'
          });
        }
      });
  }

  private updateLegalAct(dto: LegalActDTO): void {
    dto.id = this.legalAct.id;
    dto.entityVersion = this.legalAct.entityVersion;
    this.legalActService.update(dto)
      .pipe(finalize(() => this.loading = false))
      .subscribe((updatedLegalAct: LegalActDTO) => {
        if (updatedLegalAct) {
          this.afterUpsertData(updatedLegalAct, 'common.updated');
        }
      });
  }

  private afterUpsertData(changedDTO: LegalActDTO, translationKeyForSuccess: string): void {
    this.legalAct = changedDTO;
    const translateMessage: string = this.translateService.instant(translationKeyForSuccess);
    this.snackBar.open(translateMessage, 'OK', {duration: 2000});
  }

  private initLegalActForUpdate(params: Params): void {
    if (!params) {
      return;
    }
    const legalActId: number = +params[RouteProjectManagement.LEGAL_ACTS_UPSERT_ID_PARAM];
    if (!legalActId) {
      return;
    }
    if (this.legalAct && this.legalAct.id === legalActId) {
      return;
    }
    this.loadLegalActFromServer(legalActId);
  }

  private loadLegalActFromServer(legalActId: number): void {
    this.loading = true;
    this.legalActService.findById(legalActId)
      .pipe(finalize(() => this.loading = false))
      .subscribe((legalAct: LegalActDTO) => {
        this.legalAct = legalAct;
        this.patchFormValue(legalAct);
      });
  }

  private patchFormValue(legalAct: LegalActDTO): void {
    this.form.patchValue(legalAct);
    const date: Date = DateTimeUtils.stringToDate(legalAct.dateOfStr);
    this.form.controls[this.ctrlDateOf].setValue(date);
  }

}
