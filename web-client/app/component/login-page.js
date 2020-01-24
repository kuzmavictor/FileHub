import {LoginForm} from './login-form.js';
import {ErrorBlock} from './error-block.js';
import {StatefulComponent} from './stateful-component.js';

/**
 * A login page component.
 */
export class LoginPage extends StatefulComponent {
  /**
   * Initializes parameters for login page rendering.
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
   * Renders login page.
   */
  render() {
    this._page = document.createElement('div');
    this._page.className = 'container wrapper user-form';

    this._page.innerHTML = `
        <h1 class="form-header">Login</h1>
        <hr class="delimeter">
    `;
    const loginForm = new LoginForm(this._page);
    this.parentNode.appendChild(this._page);

    loginForm.onSubmit((login, password) => {
      this._handler(login, password);
    });
  }

  /**
   * Adds the handler for login form submit.
   *
   * @param {Function} handler a callback that will be executed after submitting login form
   */
  onSubmit(handler) {
    this._handler = handler;
  }
}
