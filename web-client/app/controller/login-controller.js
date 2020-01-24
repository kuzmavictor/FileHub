import {LoginPage} from '../component/login-page.js';

/**
 * A controller for the login page.
 */
export class LoginController {
  /**
   * Initializes a {@code LoginController} instance.
   *
   * @param {HTMLElement} parentNode
   * @param {Router} router
   * @param {Store} store
   */
  constructor(parentNode, router, store) {
    this._parentNode = parentNode;
    this._router = router;
    this._store = store;
    this._loginHandler();
  }

  /**
   * Creates the login page and handles submitting the login form.
   *
   * @private
   */
  _loginHandler() {
    document.title = 'Log in';
    const loginPage = new LoginPage(this._store, this._parentNode);

    loginPage.onSubmit((login, password) => {
      this._store.dispatch('login', login, password)
          .then(() => this._router.redirect('#/file-list'))
          .catch((error) => {
            console.log(error.message);
          });
    });
  }
}
