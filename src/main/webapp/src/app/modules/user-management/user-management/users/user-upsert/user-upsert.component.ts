import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ValidationErrors} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {finalize} from 'rxjs/operators';
import {RoleDTO, UserDTO, UserUpsertDTO} from '../../../../../common/api/api-models';
import {RouteUserManagement} from '../../../../../common/const/routes';
import {EmailValidator} from '../../../../../common/validators/email.validator';
import {RequiredValidator} from '../../../../../common/validators/required.validator';
import {UniqueAsyncValidator} from '../../../../../common/validators/unique.async-validator';
import {RoleService} from '../../../services/role.service';
import {UserManagementService} from '../../../services/user-management.service';

@Component({
  templateUrl: './user-upsert.component.html',
  styleUrls: ['./user-upsert.component.scss']
})
export class UserUpsertComponent implements OnInit {

  readonly ctrlEmail: string = 'email';
  readonly ctrlFirstName: string = 'firstName';
  readonly ctrlLastName: string = 'lastName';
  readonly ctrlRole: string = 'role';
  readonly ctrlPassword: string = 'password';
  readonly ctrlPasswordRepeat: string = 'passwordRepeat';
  readonly ctrlPhoneNumber: string = 'phoneNumber';
  readonly ctrlDescription: string = 'description';

  readonly prefixNumberPhone: string = '+48';

  userUpsert: UserUpsertDTO;
  availableRoles: RoleDTO[] = [];

  loading: boolean = false;

  hidePassword: boolean = true;
  hidePasswordRepeat: boolean = true;

  form: FormGroup;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    private snackBar: MatSnackBar,
    private roleService: RoleService,
    private userManagementService: UserManagementService
  ) {
  }

  ngOnInit(): void {
    this.loadAvailableRoles();
    this.initForm();
    this.activatedRoute.params.subscribe((params: Params) => this.initRoleForUpdate(params));
  }

  blurPassword(): void {
    const password: string = this.form.controls[this.ctrlPassword].value;
    const passwordRepeat: string = this.form.controls[this.ctrlPasswordRepeat].value;
    if (!password || !passwordRepeat) {
      return;
    }
    if (password !== passwordRepeat) {
      const error: ValidationErrors = {['passwordsDoNotMatch']: true};
      this.form.controls[this.ctrlPassword].setErrors(error);
      this.form.controls[this.ctrlPasswordRepeat].setErrors(error);
    }
  }

  clickSave(): void {
    const dto: UserUpsertDTO = this.buildDTO();
    if (!this.userUpsert) {
      this.createUser(dto);
    } else {
      this.updateUser(dto);
    }
  }

  private createUser(dto: UserUpsertDTO): void {
    this.userManagementService.create(dto)
      .pipe(finalize(() => this.loading = false))
      .subscribe((createdUser: UserUpsertDTO) => {
        if (createdUser) {
          this.router.navigate([createdUser.user.id], {
            relativeTo: this.activatedRoute,
            queryParamsHandling: 'merge'
          });
          this.afterUpsertData(createdUser, 'common.saved');
        }
      });
  }

  private updateUser(dto: UserUpsertDTO): void {
    dto.user.id = this.userUpsert.user.id;
    dto.user.entityVersion = this.userUpsert.user.entityVersion;
    this.userManagementService.update(dto)
      .pipe(finalize(() => this.loading = false))
      .subscribe((updatedUser: UserUpsertDTO) => {
        if (updatedUser) {
          this.afterUpsertData(updatedUser, 'common.updated');
        }
      });
  }

  private afterUpsertData(changedDTO: UserUpsertDTO, translationKeyForSuccess: string): void {
    this.userUpsert = changedDTO;
    this.removeValidatorsFromPassword();
    this.setExistsUserEmailAsyncValidator();
    const translateMessage: string = this.translateService.instant(translationKeyForSuccess);
    this.snackBar.open(translateMessage, 'OK', {duration: 2000});
  }

  private initForm(): void {
    this.form = this.formBuilder.group({
      [this.ctrlEmail]: [null, [RequiredValidator.requiredTrim, EmailValidator.email]],
      [this.ctrlFirstName]: [null, RequiredValidator.requiredTrim],
      [this.ctrlLastName]: [null, RequiredValidator.requiredTrim],
      [this.ctrlRole]: [null, RequiredValidator.required],
      [this.ctrlPassword]: [null, RequiredValidator.required],
      [this.ctrlPasswordRepeat]: [null, RequiredValidator.required],
      [this.ctrlPhoneNumber]: null,
      [this.ctrlDescription]: null
    });
    this.setExistsUserEmailAsyncValidator();
  }

  private setExistsUserEmailAsyncValidator(): void {
    const userId: number = this.userUpsert ? this.userUpsert.user.id : null;
    this.form.controls[this.ctrlEmail].setAsyncValidators(
      UniqueAsyncValidator.uniqueFn(this.userManagementService, 'existsUserEmail', 'notUnique', userId)
    );
    this.form.controls[this.ctrlEmail].updateValueAndValidity();
  }

  private loadAvailableRoles(): void {
    this.loading = true;
    this.roleService.allAvailable()
      .pipe(finalize(() => this.loading = false))
      .subscribe((roles: RoleDTO[]) => {
        this.availableRoles = roles;
        if (this.userUpsert) {
          this.patchRoleFormValue(this.userUpsert);
        }
      });
  }

  private initRoleForUpdate(params: Params): void {
    if (!params) {
      return;
    }
    const userId: number = +params[RouteUserManagement.USERS_UPSERT_ID_PARAM];
    if (!userId) {
      return;
    }
    if (this.userUpsert && this.userUpsert.user && this.userUpsert.user.id === userId) {
      return;
    }
    this.loadUserFromServer(userId);
  }

  private loadUserFromServer(userId: number): void {
    this.loading = true;
    this.userManagementService.findUpsertById(userId)
      .pipe(finalize(() => this.loading = false))
      .subscribe((userUpsert: UserUpsertDTO) => {
        this.userUpsert = userUpsert;
        this.patchFormValue(userUpsert);
        this.removeValidatorsFromPassword();
        this.setExistsUserEmailAsyncValidator();
      });
  }

  private patchFormValue(userUpsert: UserUpsertDTO): void {
    this.form.patchValue(userUpsert.user);
    this.patchRoleFormValue(userUpsert);
  }

  private patchRoleFormValue(userUpsert: UserUpsertDTO): void {
    const selectedRole: RoleDTO = this.availableRoles.find(s => s.id === userUpsert.role.id);
    this.form.controls[this.ctrlRole].setValue(selectedRole);
  }

  private removeValidatorsFromPassword(): void {
    this.removeValidator(this.ctrlPassword);
    this.removeValidator(this.ctrlPasswordRepeat);
  }

  private removeValidator(ctrlName: string): void {
    this.form.controls[ctrlName].setValue(null);
    this.form.controls[ctrlName].clearValidators();
    this.form.controls[ctrlName].updateValueAndValidity();
  }

  private buildDTO(): UserUpsertDTO {
    return {
      user: this.buildInnerUserDTO(),
      role: this.form.controls[this.ctrlRole].value,
      password: this.form.controls[this.ctrlPassword].value
    };
  }

  private buildInnerUserDTO(): UserDTO {
    let phoneNumber: string = this.form.controls[this.ctrlPhoneNumber].value;
    if (phoneNumber && !phoneNumber.startsWith(this.prefixNumberPhone)) {
      phoneNumber = this.prefixNumberPhone + phoneNumber;
    }
    return {
      email: this.form.controls[this.ctrlEmail].value,
      firstName: this.form.controls[this.ctrlFirstName].value,
      lastName: this.form.controls[this.ctrlLastName].value,
      phoneNumber: phoneNumber,
      description: this.form.controls[this.ctrlDescription].value
    };
  }

}
