import {RegistrationPage} from '../component/registration-page.js';

/**
 * A controller for the registration page.
 */
export class RegistrationController {
  /**
   * Initializes a {@code RegistrationController} instance.
   *
   * @param {HTMLElement} parentNode
   * @param {Router} router
   * @param {Store} store
   */
  constructor(parentNode, router, store) {
    this._parentNode = parentNode;
    this._router = router;
    this._store = store;
    this._registrationHandler();
  }

  /**
   * Renders a registration page and handles submitting the registration form.
   *
   * @private
   */
  _registrationHandler() {
    document.title = 'Registration';
    const registrationPage = new RegistrationPage(this._store, this._parentNode);

    registrationPage.onSubmit((user) => {
      this._store.dispatch('register', user)
          .then(() => this._router.redirect('#/login'))
          .catch((error) => {
            console.log(error.message);
          });
    });
  }
}
