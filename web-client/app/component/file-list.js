import {FileListItem} from './file-list-item.js';
import {StatefulComponent} from './stateful-component.js';
import {DragEventHandler} from './drag-event-handler.js';

/**
 * Stands for file list object, that can contain files and folders.
 */
export class FileList extends StatefulComponent {
  /**
   * @param {Store} store
   * @param {HTMLElement} parentNode
   */
  constructor(store, parentNode) {
    super(store, parentNode);
    this._dragEventHandler = new DragEventHandler();
    this.store.events.subscribe('stateChange.files', () => this.render());
    this.store.events.subscribe('stateChange.filesLoading',
        () => this._showMessage(this.store.state.filesLoading, 'Loading...'));
    this.store.events.subscribe('stateChange.filesMoving',
        () => this._showMessage(this.store.state.filesMoving, 'Moving...'));
    this.store.events.subscribe('stateChange.filesUploading',
        () => this._showMessage(this.store.state.filesUploading, 'Uploading...'));
    this.store.events.subscribe('stateChange.filesRemoving',
        () => this._showMessage(this.store.state.filesRemoving, 'Removing...'));
    this.store.events.subscribe('stateChange.fileRenaming',
        () => this._showMessage(this.store.state.fileRenaming, 'Renaming...'));
    this.store.events.subscribe('stateChange.folderCreating', () => {
      this._showMessage(this.store.state.folderCreating, 'Folder creating...');
    });
    this._initialRender();
  }

  /**
   * Initial render for file list object.
   */
  _initialRender() {
    this._fileList = document.createElement('div');
    this._fileList.classList.add('container-fluid', 'pre-scrollable', 'file-list-box');
    const header = document.createElement('div');
    header.className = `row list-item fixed-bar`;
    header.innerHTML = `
        <div class="col-xs-4 col-md-3 cell">
            File name 
        </div>
        <div class="col-xs-2 hidden-xs hidden-sm cell">
            Upload date
        </div>
        <div class="col-xs-2 cell">
            File size
        </div>
        <div class="col-md-2 hidden-xs hidden-sm cell">
            Owner
        </div>
        <div class="col-xs-2 col-md-1 cell">
            Type
        </div>`;
    this._fileList.appendChild(header);

    this._listItems = document.createElement('ul');
    this._listItems.className = 'list-unstyled list-items';
    this._fileList.appendChild(this._listItems);
    this.parentNode.appendChild(this._fileList);
    this._loadingBlock = document.createElement('div');
    this.parentNode.appendChild(this._loadingBlock);
    this._addDragAndDropEventListeners();
  }

  /**
   * Adds loading status to file list page while file list is updating.
   * @param {boolean} flag
   * @param {string} text
   * @private
   */
  _showMessage(flag, text) {
    if (flag) {
      this._loadingBlock.classList.add('loading-block', 'col-xs-12');
      this._loadingBlock.innerHTML = `<div class="loading-text"><p>${text}</p></div>`;
    } else {
      this._loadingBlock.classList.remove('loading-block');
      this._loadingBlock.innerHTML = ``;
    }
  }

  /**
   * Renders file list.
   */
  render() {
    this._listItems.innerHTML = '';
    this.store.state.files.forEach((element) => {
      const fileListItem = new FileListItem(this._listItems, element);
      fileListItem.onFileFromOsDropped((files, folderId) => {
        Array.from(files).forEach((file) => {
          this.store.dispatch('uploadFile', file, folderId);
        });
      });
      fileListItem.onFileFromListDropped((fileId, folderId) => {
        this.store.dispatch('moveFile', fileId, folderId);
      });
      fileListItem.onDeleteButtonClick(() => {
        this.store.dispatch('removeFile', element);
      });
      fileListItem.onInfoButtonClick(() => {
        this.store.dispatch('selectFile', element);
      });
      fileListItem.onShareButtonClick((id) => {
        fileListItem.createPopup();
        fileListItem.changePopupText('Loading...');
        this.store.dispatch('shareFile', id).then((URL) => {
          fileListItem.changePopupText(URL);
        });
      });
      fileListItem.onClick(() => {
        this.store.dispatch('navigate', element);
      });
      fileListItem.onRename((id, newName) => {
        this.store.dispatch('renameFile', id, newName);
      });
    });
  }

  /**
   * Adds event listener for `dragleave` event for file list.
   * @private
   */
  _addDragAndDropEventListeners() {
    this._dragEventHandler.handleDragStylesChanges(this._fileList);
    this._listItems.addEventListener('drop', (event) => {
      if (event.dataTransfer.files.length > 0) {
        Array.from(event.dataTransfer.files).forEach((file) => {
          this.store.dispatch('uploadFile', file,
              this.store.state.currentFolder.recordId.value);
        });
      }
    });
  }
}
