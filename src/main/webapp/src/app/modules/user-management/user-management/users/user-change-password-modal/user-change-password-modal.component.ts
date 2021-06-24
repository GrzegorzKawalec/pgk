import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ValidationErrors} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {finalize} from 'rxjs/operators';
import {UserChangePasswordDTO, UserDTO} from '../../../../../common/api/api-models';
import {UserService} from '../../../../../common/services/user.service';
import {RequiredValidator} from '../../../../../common/validators/required.validator';
import {AuthService} from '../../../../../core/auth/auth.service';
import {UserManagementService} from '../../../services/user-management.service';

@Component({
  templateUrl: './user-change-password-modal.component.html',
  styleUrls: ['./user-change-password-modal.component.scss']
})
export class UserChangePasswordModalComponent implements OnInit {

  readonly user: UserDTO;

  readonly ctrlPassword: string = 'password';
  readonly ctrlPasswordRepeat: string = 'passwordRepeat';

  hidePassword: boolean = true;
  hidePasswordRepeat: boolean = true;

  form: FormGroup;

  loading: boolean;

  constructor(
    @Inject(MAT_DIALOG_DATA) data: UserDTO,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private userManagementService: UserManagementService,
    private dialogRef: MatDialogRef<UserChangePasswordModalComponent>
  ) {
    this.user = data;
  }

  ngOnInit(): void {
    this.initForm();
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

  changePassword(): void {
    if (this.loading || !this.form.valid) {
      return;
    }
    this.loading = true;
    const dto: UserChangePasswordDTO = this.buildDTO();
    this.userManagementService.changePassword(dto)
      .pipe(finalize(() => this.loading = false))
      .subscribe(() => this.afterChangePassword());
  }

  private initForm(): void {
    this.form = this.formBuilder.group({
      [this.ctrlPassword]: [null, RequiredValidator.required],
      [this.ctrlPasswordRepeat]: [null, RequiredValidator.required]
    });
  }

  private buildDTO(): UserChangePasswordDTO {
    return {
      userId: this.user.id,
      password: this.form.controls[this.ctrlPassword].value
    };
  }

  private afterChangePassword(): void {
    const loggedUser: UserDTO = this.userService.loggedUser;
    if (this.user.id === loggedUser.id) {
      this.authService.signOut();
    }
    this.dialogRef.close(true);
  }

}
