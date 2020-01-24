/**
 * Contains fields that describe one user.
 */
export class User {
  /**
   * @param {string} login
   * @param {string} password
   * @param {File} avatar
   */
  constructor(login, password, avatar) {
    this._login = login;
    this._password = password;
    this._avatar = avatar;
  }

  /**
   * Returns user login value.
   * @return {string}
   */
  get login() {
    return this._login;
  }

  /**
   * Sets user login value.
   * @param {string} value
   */
  set login(value) {
    this._login = value;
  }

  /**
   * Returns user password value.
   * @return {string}
   */
  get password() {
    return this._password;
  }

  /**
   * Sets user password value.
   * @param {string} value
   */
  set password(value) {
    this._password = value;
  }

  /**
   * Returns user avatar file.
   * @return {string}
   */
  get avatar() {
    return this._avatar;
  }

  /**
   * Sets user avatar file value.
   * @param {string} value
   */
  set avatar(value) {
    this._avatar = value;
  }
}
