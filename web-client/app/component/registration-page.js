import {RegistrationForm} from './registration-form.js';
import {ErrorBlock} from './error-block.js';
import {StatefulComponent} from './stateful-component.js';

/**
 * A registration page component.
 */
export class RegistrationPage extends StatefulComponent {
  /**
   * Initializes parameters for rendering registration page.
   *
   * @param {Store} store storage of the application state
   * @param {HTMLElement} parentNode a root of DOM hierarchy
   */
  constructor(store, parentNode) {
    super(store, parentNode);

    this.store.events.subscribe('stateChange.errorMessage', () => {
      new ErrorBlock(this._page, this.store.state.errorMessage);
    });
    this.render();
  }

  /**
   * Renders registration page.
   */
  render() {
    this._page = document.createElement('div');
    this._page.className = 'container wrapper user-form';
    this._page.innerHTML = `
        <h1 class="form-header">Registration</h1>
        <hr class="delimeter">`;
    const registrationForm = new RegistrationForm(this._page);
    this.parentNode.appendChild(this._page);
    registrationForm.onSubmit((user) => {
      this._handler(user);
    });
  }

  /**
   * Adds the handler for registration form submission.
   *
   * @param {Function} handler a callback that will be executed after submitting registration form
   */
  onSubmit(handler) {
    this._handler = handler;
  }
}
