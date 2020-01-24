import {Button} from './button.js';
import {InputField} from './input-field.js';
import {FormValidator} from '../service/form-validator.js';
import {Component} from './component.js';

/**
 * Stands for form object. Form contains input fields, submit button etc.
 */
export class LoginForm extends Component {
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
    this.form = document.createElement('form');
    const formInnerWrapper = document.createElement('div');
    formInnerWrapper.className = 'col-xs-12';

    this._login = new InputField(formInnerWrapper, 'login', 'text', 'Login: ');
    this._password = new InputField(formInnerWrapper, 'password', 'password', 'Password: ');

    const formFooter = document.createElement('div');
    formFooter.className = 'form-group';
    formFooter.innerHTML = `
       <a class="col-xs-4 form-page-link" href="#/registration">Not registered?</a>
    `;
    this._button = new Button(formFooter, 'Login',
        'btn col-xs-2 col-xs-offset-6 submit-button');
    this.form.appendChild(formInnerWrapper);
    this.form.appendChild(formFooter);
    this.parentNode.appendChild(this.form);
  }

  /**
   * Adds error message to form if something went wrong.
   * @param {string} message
   */
  addErrorMessage(message) {
    if (!!message) {
      const errorMessage = document.createElement('span');
      errorMessage.innerHTML = `${message}`;
      this.form.appendChild(errorMessage);
      this._button.enable();
    }
  }

  /**
   * Sets `submit` event.
   * @param {Function} callback
   */
  onSubmit(callback) {
    this._button.onClick(() => {
      if (this.validate()) {
        this._button.disable();
        callback(this._login.value, this._password.value);
        this._button.enable();
      }
    });
  }

  /**
   * Validates form input fields.
   * @return {boolean}
   */
  validate() {
    const loginValidationResult = FormValidator.validateLogin(this._login.value);
    this._login.errorField = loginValidationResult;

    const passwordValidationResult = FormValidator.validatePassword(this._password.value);
    this._password.errorField = passwordValidationResult;

    return (!loginValidationResult && !passwordValidationResult);
  }
}
