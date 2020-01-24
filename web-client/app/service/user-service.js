/**
 * Operates RequestService to get user information, register new user or user log in.
 */
export class UserService {
  /**
   * @param {RequestService} requestService
   */
  constructor(requestService) {
    this._requestService = requestService;
  }

  /**
   * Sends POST request for user registration.
   *
   * @param {User} user the user registration data
   * @return {Promise}
   */
  register(user) {
    const formData = new FormData();
    formData.append('login', user.login);
    formData.append('password', user.password);
    formData.append('avatar', user.avatar);

    return this._requestService.post('/register', formData);
  }

  /**
   * Sends GET request for user log in.
   *
   * @param {Object} logInData the user data from authorization form.
   * @return {Promise}
   */
  logIn(logInData) {
    const formData = new FormData();
    formData.append('login', logInData.login);
    formData.append('password', logInData.password);

    return this._requestService.post('/login', formData).then((response) => {
      return response.json().then((body) => {
        this._requestService.token = body.token.value;
        return body;
      });
    });
  }

  /**
   * Sends GET request to get user info.
   * @param {string} id
   * @return {Promise}
   */
  getUserInfo(id) {
    return this._requestService.get(id);
  }
}
