import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {finalize} from 'rxjs/operators';
import {Authority, RoleDTO} from '../../../../../common/api/api-models';
import {RouteUserManagement} from '../../../../../common/const/routes';
import {RequiredValidator} from '../../../../../common/validators/required.validator';
import {UniqueAsyncValidator} from '../../../../../common/validators/unique.async-validator';
import {RoleService} from '../../../services/role.service';

@Component({
  templateUrl: './roles-upsert.component.html',
  styleUrls: ['./roles-upsert.component.scss']
})
export class RolesUpsertComponent implements OnInit {

  readonly ctrlName: string = 'name';
  readonly ctrlAuthorities: string = 'authorities';
  readonly ctrlDescription: string = 'description';

  loading: boolean = false;

  role: RoleDTO;
  form: FormGroup;
  selectedAuthorities: SelectAuthorityModel[] = [];
  selectAuthorityModels: SelectAuthorityModel[] = [];

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    private snackBar: MatSnackBar,
    private roleService: RoleService
  ) {
  }

  ngOnInit(): void {
    this.initForm();
    this.roleService.getAllAuthorities().subscribe((allAuthorities: Authority[]) => this.prepareSelectAuthorityModels(allAuthorities));
    this.activatedRoute.params.subscribe((params: Params) => this.initRoleForUpdate(params));
  }

  clickSave(): void {
    if (this.loading || !this.form.valid) {
      return;
    }
    this.changeLoadingState();
    const dto: RoleDTO = this.buildDTO();
    if (!this.role) {
      this.createRole(dto);
    } else {
      this.updateRole(dto);
    }
  }

  private changeLoadingState(): void {
    this.loading = !this.loading;
    if (this.loading) {
      this.form.disable({onlySelf: true});
    } else {
      this.form.enable({onlySelf: true})
    }
  }

  private createRole(dto: RoleDTO): void {
    this.roleService.create(dto)
      .pipe(finalize(() => this.changeLoadingState()))
      .subscribe((createdRole: RoleDTO) => {
        if (createdRole) {
          this.afterUpsertData(createdRole, 'common.saved');
          this.router.navigate([createdRole.id], {
            relativeTo: this.activatedRoute,
            queryParamsHandling: 'merge'
          });
        }
      });
  }

  private updateRole(dto: RoleDTO): void {
    dto.id = this.role.id;
    dto.entityVersion = this.role.entityVersion;
    this.roleService.update(dto)
      .pipe(finalize(() => this.changeLoadingState()))
      .subscribe((updatedRole: RoleDTO) => {
        if (updatedRole) {
          this.afterUpsertData(updatedRole, 'common.updated');
        }
      });
  }

  private afterUpsertData(changedDTO: RoleDTO, translationKeyForSuccess: string): void {
    this.role = changedDTO;
    this.setExistsRoleNameAsyncValidator();
    const translateMessage: string = this.translateService.instant(translationKeyForSuccess);
    this.snackBar.open(translateMessage, 'OK', {duration: 2000});
  }

  private initForm(): void {
    this.form = this.formBuilder.group({
      [this.ctrlName]: [null, RequiredValidator.requiredTrim],
      [this.ctrlAuthorities]: [null, RequiredValidator.required],
      [this.ctrlDescription]: null
    });
    this.setExistsRoleNameAsyncValidator();
  }

  private setExistsRoleNameAsyncValidator(): void {
    const roleId: number = this.role ? this.role.id : null;
    this.form.controls[this.ctrlName].setAsyncValidators(
      UniqueAsyncValidator.uniqueFn(this.roleService, 'existsRoleName', 'notUnique', roleId)
    );
  }

  private prepareSelectAuthorityModels(allAuthorities: Authority[]): void {
    this.selectAuthorityModels = this.prepareAuthorityModels(allAuthorities);
  }

  private prepareAuthorityModels(authorities: Authority[]): SelectAuthorityModel[] {
    if (!authorities || authorities.length < 1) {
      return [];
    }
    return authorities.map(authority => {
      const translateKey: string = 'authorities.' + authority + '.';
      return {
        disabled: Authority.ADMIN === authority,
        authority: authority,
        translateName: this.translateService.instant(translateKey + 'name'),
        translateDescription: this.translateService.instant(translateKey + 'description')
      };
    }).sort((a, b) => (a.translateName > b.translateName) ? 1 : ((b.translateName > a.translateName) ? -1 : 0));
  }

  private initRoleForUpdate(params: Params): void {
    if (!params) {
      return;
    }
    const roleId: number = +params[RouteUserManagement.ROLES_UPSERT_ID_PARAM];
    if (!roleId) {
      return;
    }
    if (this.role && this.role.id === roleId) {
      return;
    }
    this.loadRoleFromServer(roleId);
  }

  private loadRoleFromServer(roleId: number): void {
    this.roleService.findById(roleId).subscribe((role: RoleDTO) => {
      this.role = role;
      this.patchFormValue(role);
      this.setExistsRoleNameAsyncValidator();
    });
  }

  private patchFormValue(role: RoleDTO): void {
    this.form.patchValue(role);
    const authorities: SelectAuthorityModel[] = this.prepareAuthorityModels(role.authorities);
    const models: SelectAuthorityModel[] = this.selectAuthorityModels.filter((model: SelectAuthorityModel) => {
      return authorities.find(authority => authority.authority === model.authority);
    });
    this.form.controls[this.ctrlAuthorities].setValue(models);
  }

  private buildDTO(): RoleDTO {
    const authorities: SelectAuthorityModel[] = this.form.controls[this.ctrlAuthorities].value || [];
    return {
      name: this.form.controls[this.ctrlName].value,
      description: this.form.controls[this.ctrlDescription].value,
      authorities: authorities.map(a => a.authority)
    };
  }

}

interface SelectAuthorityModel {
  disabled: boolean;
  authority: Authority,
  translateName: string;
  translateDescription: string;
}