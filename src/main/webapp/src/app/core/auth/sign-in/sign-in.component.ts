import {Component} from '@angular/core';
import {FormControl, ValidationErrors} from '@angular/forms';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {finalize, takeUntil} from 'rxjs/operators';
import {BaseComponent} from '../../../common/components/base.component';
import {EmailValidator} from '../../../common/validators/email.validator';
import {RequiredValidator} from '../../../common/validators/required.validator';
import {AuthService} from '../auth.service';

@Component({
  selector: 'pgk-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent extends BaseComponent {

  showSpinner: boolean = false;
  hidePassword: boolean = true;

  emailControl: FormControl = new FormControl('', [RequiredValidator.requiredTrim, EmailValidator.email]);
  passwordControl: FormControl = new FormControl('', [RequiredValidator.required]);

  constructor(
    private translateService: TranslateService,
    private authService: AuthService,
    private router: Router
  ) {
    super();
  }

  keyDownFromInput(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.signIn();
    }
  }

  signIn(): void {
    if (this.emailControl.invalid || this.passwordControl.invalid) {
      return;
    }
    this.showSpinner = true;
    const username: string = this.emailControl.value.trim();
    const password: string = this.passwordControl.value;
    this.authService.signIn(username, password).subscribe(() => {
      const urlToRedirect: string = this.prepareUrlToRedirect();
      this.authService.loggedUser$
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => this.showSpinner = false)
        ).subscribe(() => this.router.navigateByUrl(urlToRedirect));
    });
  }

  private prepareUrlToRedirect(): string {
    const urlToRedirect: string = this.authService.urlBeforeRedirectToSignIn;
    this.authService.urlBeforeRedirectToSignIn = null;
    return urlToRedirect ? '/' + urlToRedirect : '/';
  }

  getErrorMessage(formControl: FormControl): string {
    return formControl === this.passwordControl ?
      this.getPasswordErrorMessage() : this.getEmailErrorMessage();
  }

  private getPasswordErrorMessage(): string {
    const error: ValidationErrors = this.passwordControl.errors;
    let translateKey: string;
    if (RequiredValidator.requiredErr(error)) {
      translateKey = 'empty-password';
    }
    return this.translateErrorMessage(translateKey);
  }

  private getEmailErrorMessage(): string {
    const error: ValidationErrors = this.emailControl.errors;
    let translateKey: string;
    if (RequiredValidator.requiredTrimErr(error)) {
      translateKey = 'empty-email';
    } else if (EmailValidator.emailErr(error)) {
      translateKey = 'incorrect-email';
    }
    return this.translateErrorMessage(translateKey);
  }

  private translateErrorMessage(errorTranslateKey: string): string {
    return !errorTranslateKey ? '' : this.translateService.instant('sign-in.invalid-form.' + errorTranslateKey);
  }

}
