import {Button} from './button.js';
import {InputField} from './input-field.js';
import {FormValidator} from '../service/form-validator.js';
import {AvatarInputField} from './avatar-input-field.js';
import {Component} from './component.js';

/**
 * Stands for form object. Form contains input fields, submit button etc.
 */
export class RegistrationForm extends Component {
  /**
   * @param {HTMLElement} parentNode
   */
  constructor(parentNode) {
    super(parentNode);
    this.render();
  }

  /**
   * Renders and displays form.
   */
  render() {
    this._form = document.createElement('form');
    const formInnerWrapper = document.createElement('div');
    formInnerWrapper.className = 'col-xs-12';

    const fileWrapper = document.createElement('div');
    fileWrapper.className = 'col-xs-6';
    formInnerWrapper.appendChild(fileWrapper);

    this._file = new AvatarInputField(fileWrapper);

    const inputsWrapper = document.createElement('div');
    inputsWrapper.className = 'col-xs-6';
    this._login = new InputField(inputsWrapper,
        'registration-login', 'text', 'Login: ');
    this._password = new InputField(inputsWrapper,
        'registration-password', 'password', 'Password: ');
    this._secondPassword = new InputField(inputsWrapper,
        'registration-second-password', 'password', 'Repeat password: ');
    formInnerWrapper.appendChild(inputsWrapper);

    this._file.onChange(() => {
      if (this._file.inputFiles[0]) {
        this._file.displayImage();
      } else {
        this._file.errorField = 'No file chosen!';
      }
    });
    this._form.appendChild(formInnerWrapper);
    const formFooter = document.createElement('div');
    formFooter.className = 'col-xs-12';
    formFooter.innerHTML = `
       <a class="col-xs-4 form-page-link" href="#/login">Have an account?</a>
    `;

    this._button = new Button(formFooter, 'Register',
        'btn col-xs-2 col-xs-offset-6 submit-button');
    this._form.appendChild(formFooter);
    this.parentNode.appendChild(this._form);
  }

  /**
   * @return {Object}
   */
  get user() {
    return {login: this._login.value,
      password: this._password.value,
      avatar: this._file.avatarContent};
  }

  /**
   * Adds error message to form if something went wrong.
   * @param {string} message
   */
  addErrorMessage(message) {
    const errorMessage = document.createElement('span');
    errorMessage.innerHTML = `${message}`;
    this._form.appendChild(errorMessage);
  }

  /**
   * @param {Function} callback
   */
  onSubmit(callback) {
    this._button.onClick(() => {
      if (this.validate()) {
        this._button.disable();
        callback(this.user);
        this._button.enable();
      }
    });
  }

  /**
   * Validates form input fields.
   * @return {boolean}
   */
  validate() {
    const avatarValidationResult = FormValidator.validateAvatar(this._file.inputFiles[0]);
    this._file.errorField = avatarValidationResult;

    const loginValidationResult = FormValidator.validateLogin(this._login.value);
    this._login.errorField = loginValidationResult;

    const passwordValidationResult = FormValidator.validatePasswords(this._password.value,
        this._secondPassword.value);
    this._password.errorField = passwordValidationResult;
    this._secondPassword.errorField = passwordValidationResult;

    return (!avatarValidationResult && !loginValidationResult && !passwordValidationResult);
  }
}
