import {FileListPage} from '../component/file-list-page.js';

/**
 * A controller for the login page.
 */
export class FileListController {
  /**
   * Initializes a {@code FileListController} instance.
   *
   * @param {HTMLElement} parentNode
   * @param {Router} router
   * @param {Store} store
   */
  constructor(parentNode, router, store) {
    this._parentNode = parentNode;
    this._store = store;
    this._fileListHandler();
  }

  /**
   * Creates the file list page.
   *
   * @private
   */
  _fileListHandler() {
    document.title = 'File list';
    new FileListPage(this._store, this._parentNode);
  }
}
