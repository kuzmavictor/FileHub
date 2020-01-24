import {UserService} from './user-service.js';
import {FileService} from './file-service.js';
import {RequestService} from './request-service.js';

/**
 * Simple service factory for creating service instances.
 */
export class ServiceFactory {
  /**
   * Creates service factory instance.
   *
   * @param {string} serverUrl a URL where the requests will be sent to
   */
  constructor(serverUrl) {
    this._requestService = new RequestService(serverUrl);
    this._requestService.onPageNotFound(() => {
      this._pageNotFoundHandler && this._pageNotFoundHandler();
    });
    this._userService = new UserService(this._requestService);
    this._fileService = new FileService(this._requestService);
  }

  /**
   * @return {UserService}
   */
  getUserServiceInstance() {
    return this._userService;
  }

  /**
   * @return {FileService}
   */
  getFileServiceInstance() {
    return this._fileService;
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
}
