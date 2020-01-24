import {FileList} from './file-list.js';
import {UploadButton} from './upload-button.js';
import {FileInfoBlock} from './file-info-block.js';
import {StatefulComponent} from './stateful-component.js';
import {CreateFolderControl} from './create-folder-control.js';
import {ErrorBlock} from './error-block.js';

/**
 * Stands for file list page.
 */
export class FileListPage extends StatefulComponent {
  /**
   * @param {Store} store
   * @param {HTMLElement} parentNode
   */
  constructor(store, parentNode) {
    super(store, parentNode);
    this.store.events.subscribe('stateChange.currentFolder', () => {
      this._displayCurrentFolderName(this.store.state.currentFolder.name);
    });
    this.store.dispatch('fetchFiles').then(() => {
      this.store.events.subscribe('stateChange.errorMessage', () => {
        new ErrorBlock(this._container, this.store.state.errorMessage);
      });
      this.store.events.subscribe('stateChange.files', () => {
        this._createFolderButton.hasError(false);
        this._createFolderButton.isFolderCreating = false;
      });
    });
    this.render();
    this._displayCurrentFolderName(this.store.state.currentFolder.name);
  }

  /**
   * Displays name of current folder.
   * @param {string} name
   * @private
   */
  _displayCurrentFolderName(name) {
    if (!this._folderName) {
      this._folderName = document.createElement('div');
      this._folderName.className = 'folder-name';
      this._container.prepend(this._folderName);
    }
    this._folderName.innerHTML = `${name}`;
  }

  /**
   * Renders file list page.
   */
  render() {
    this._container = document.createElement('div');
    this._container.className = 'container wrapper';

    const uploadButton = new UploadButton(this._container);
    uploadButton.onclick(() => {
      const files = uploadButton.files;
      Array.from(files).forEach((file) => {
        this.store.dispatch('uploadFile', file,
            this.store.state.currentFolder.recordId.value);
      });
    });

    this._createFolderButton = new CreateFolderControl(this._container);
    this._createFolderButton.onFolderCreate((folderName) => {
      this.store.dispatch('createFolder', folderName,
          this.store.state.currentFolder.recordId.value)
          .catch(() => {
            this._createFolderButton.hasError(true);
          });
    });

    this._fileListBlock = document.createElement('div');
    this._fileListBlock.className = 'files-box clearfix';
    this._container.appendChild(this._fileListBlock);

    new FileInfoBlock(this.store, this._fileListBlock);
    new FileList(this.store, this._fileListBlock);
    this.parentNode.appendChild(this._container);
  }
}
