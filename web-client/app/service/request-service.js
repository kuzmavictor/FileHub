/**
 * Sends HTTP requests to backend.
 */
export class RequestService {
  /**
   * @param {string} serverUrl a URL of server
   */
  constructor(serverUrl) {
    this._serverUrl = serverUrl;
  }

  /**
   * Sends POST request to backend.
   *
   * @param {string} url a URL to send a request
   * @param {Object} data
   * @return {Promise}
   */
  post(url, data) {
    console.log('post', url);
    console.log(data);

    return this._processFetchPromise(fetch(this._serverUrl + url, {
      method: 'POST',
      mode: 'cors',
      body: data,
      headers: {
        'security-token': this._token,
      },
    }));
  }

  /**
   * Sends GET request to backend to check if user with given login and password exists.
   *
   * @param {string} url a URL to send a request
   * @return {Promise}
   */
  get(url) {
    console.log('get', url);
    return this._processFetchPromise(fetch(this._serverUrl + url, {
      method: 'GET',
      mode: 'cors',
      headers: {
        'security-token': this._token,
      },
    }));
  }

  /**
   * Sends DELETE request to backend to delete file, folder or user.
   *
   * @param {string} url
   * @return {Promise}
   */
  delete(url) {
    console.log('delete', url);
    return this._processFetchPromise(fetch(this._serverUrl + url, {
      method: 'DELETE',
      mode: 'cors',
      headers: {
        'security-token': this._token,
      },
    }));
  }

  /**
   * Sends PUT request to backend to change file, folder or user data.
   * @param {URL} URL
   * @param {JSON} JSON
   * @return {Promise<any>}
   */
  put(URL, JSON) {
    console.log('put', URL);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve();
      }, 1000);
    });
  }

  /**
   * Handles a {@link Promise} of `fetch` method.
   *
   * @param {Promise<Response>} promise a {@link Promise} of `fetch` method
   * @return {Promise} a resolved {@code Promise} after positive server response
   * or rejected after negative server response
   * @private
   */
  _processFetchPromise(promise) {
    return promise
        .catch(() => {
          throw new Error('No access to remote resource.');
        })
        .then((response) => {
          if (response.status === 404) {
            this._pageNotFoundHandler && this._pageNotFoundHandler();
            throw new Error('The page not found.');
          }
          if (response.status >= 500) {
            throw new Error('A server error.');
          }
          if (response.status >= 400) {
            return response.json().then((body) => {
              throw new Error(body.message);
            });
          }
          return response;
        });
  }

  /**
   * Callback function that will be executed after {@code Not found (404)} error.
   *
   * @param {function} handler a callback that will be
   * executed after {@code Not found} error occurrence
   */
  onPageNotFound(handler) {
    this._pageNotFoundHandler = handler;
  }

  /**
   * Sets the token value.
   *
   * @param {string} token
   */
  set token(token) {
    this._token = token;
  }
}
