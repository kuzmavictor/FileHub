import {Component} from './component.js';
import {DragEventHandler} from './drag-event-handler.js';
import {PopperComponent} from './popper-component.js';
import {DATA_FORMAT} from './data-format.js';

/**
 * Stands for simple file list item, that contains File name, Owner,
 * file size, upload date and file type.
 */
export class FileListItem extends Component {
  /**
   * @param {HTMLElement} parentNode
   * @param {Object} file
   */
  constructor(parentNode, file) {
    super(parentNode);
    this._file = file;
    this._fileName = file.name;
    this._fileSize = 0;
    if (file.size !== 0) {
      this._fileSize = this._convertSize(file.size);
    }
    this._uploadDate = this._convertDate(file.uploadDate);
    this._owner = file.owner;
    this._type = file.type;
    this._downloadLink = file.downloadLink;
    this._id = file.id;
    this._dragEventHandler = new DragEventHandler();
    this._isRenaming = false;
    this._initialRender();
  }

  /**
   * Initial render for file list item for further rendering.
   * @private
   */
  _initialRender() {
    this._listItem = document.createElement('li');
    this._listItem.className = 'row list-item';
    this._listItem.id = this._id;
    this.parentNode.appendChild(this._listItem);
    this.render();
  }

  /**
   * Renders file list item object.
   */
  render() {
    this._listItem.innerHTML = `
        <div class="col-xs-4 col-md-3 file-name-field cell">
            ${(this._isRenaming) ? `<input type="text" value="${this._fileName}"
            class="form-control file-name-input">` : `${this._fileName}`}
        </div>
        <div class="col-xs-2 hidden-xs hidden-sm cell">
            ${this._uploadDate}
        </div>
        <div class="col-xs-2 col-md-2 cell">
            ${(this._fileSize !== 0) ? this._fileSize : ``}
        </div>
        <div class="col-md-2 hidden-xs hidden-sm cell">
            ${this._owner}
        </div>
        <div class="col-xs-2 col-md-1 cell">
            ${this._type}
        </div>
        <div class="col-xs-4 col-sm-3 col-md-2">
            <div class="btn-group btn-group-xs" role="group">
                <button title="Delete file" class="btn btn-danger delete-button" type="button">
                    <span aria-hidden="true"
                          class="glyphicon glyphicon-trash "></span>
                </button>
                <button title="Show info" class="btn btn-info info-button" type="button">
                    <span aria-hidden="true"
                          class="glyphicon glyphicon-info-sign"></span>
                </button>
                
                ${(this._type !== 'folder') ? `
                <button title="Share file" class="btn btn-primary share-button" type="button">
                    <span aria-hidden="true"
                          class="glyphicon glyphicon-share"></span>
                </button>
                <button title="Download file" class="btn btn-success download-button" type="button">
                    <span aria-hidden="true"
                          class="glyphicon glyphicon-download-alt"></span>
                </button>` : ``}
                
            </div>
        </div>`;

    this._listItem.draggable = true;
    if (this._type !== 'folder') {
      this._addDownloadButtonHandler();
    }
    this._addOnClickHandlers();
    this._addRenameHandler();
    this._addDragEventListeners();
    this._addDeleteButtonClickHandler();
    this._addInfoButtonClickHandler();
    this._addShareButtonClickHandler();
  }

  /**
   * Adds handler for delete button click action.
   * @private
   */
  _addDeleteButtonClickHandler() {
    const element = this._listItem.querySelector('.delete-button');
    element.addEventListener('click', (event) => {
      event.stopPropagation();
      this._deleteButtonHandler && this._deleteButtonHandler();
    });
  }

  /**
   * Adds handler for info button click action.
   * @private
   */
  _addInfoButtonClickHandler() {
    const element = this._listItem.querySelector('.info-button');
    element.addEventListener('click', (event) => {
      event.stopPropagation();
      this._infoButtonHandler && this._infoButtonHandler();
    });
  }

  /**
   * Adds handler for share file button click action.
   * @private
   */
  _addShareButtonClickHandler() {
    const element = this._listItem.querySelector('.share-button');
    if (this._type !== 'folder') {
      element.addEventListener('click', (event) => {
        event.stopPropagation();
        this._shareButtonHandler && this._shareButtonHandler(this._id);
      });
    }
  }

  /**
   * Adds action for delete button.
   * @param {Function} handler
   */
  onDeleteButtonClick(handler) {
    this._deleteButtonHandler = handler;
  }

  /**
   * Adds action for info block button.
   * @param {Function} handler
   */
  onInfoButtonClick(handler) {
    this._infoButtonHandler = handler;
  }

  /**
   * Adds action for download button.
   */
  _addDownloadButtonHandler() {
    const element = this._listItem.querySelector('.download-button');
    element.addEventListener('click', (event) => {
      event.stopPropagation();
      const downloadLink = document.createElement('a');
      downloadLink.setAttribute('href', this._downloadLink);
      downloadLink.setAttribute('download', this._fileName);
      downloadLink.click();
    });
  }

  /**
   * Adds action for share button.
   * @param {Function} buttonHandler
   */
  onShareButtonClick(buttonHandler) {
    this._shareButtonHandler = buttonHandler;
  }

  /**
   * Creates popup element.
   */
  createPopup() {
    this.popperComponent = new PopperComponent(this._listItem.querySelector('.btn-group'));
  }

  /**
   * Changes popup text.
   * @param {string} popupText
   */
  changePopupText(popupText) {
    this.popperComponent.changePopupText(popupText);
  }

  /**
   * Adds the {@code handleDropEvent} handler for file list item if it is a folder.
   * @param {Function} handler
   */
  onFileFromOsDropped(handler) {
    this._fileFromOsDropHandler = handler;
  }

  /**
   * Adds move {@code handler} for file list item if it is folder.
   * @param {Function} handler
   */
  onFileFromListDropped(handler) {
    this._fileFromListDropHandler = handler;
  }

  /**
   * Adds event handlers to `dragenter`, `dragover` and `dragleave` and `handleDropEvent` events.
   */
  _addDragEventListeners() {
    if (this._type === 'folder') {
      this._dragEventHandler.handleDragStylesChanges(this._listItem);
      this._listItem.addEventListener('drop', (event) => {
        if (event.dataTransfer.files.length > 0) {
          console.log(this._id);
          this._fileFromOsDropHandler(event.dataTransfer.files, this._id);
        }
        const fileData = event.dataTransfer.getData(DATA_FORMAT);
        if (fileData) {
          const file = JSON.parse(fileData);
          if (file.id !== this._id) {
            this._fileFromListDropHandler(file.id, this._id);
          }
        }
      });
    }
    this._listItem.addEventListener('dragstart', (event) => {
      event.stopPropagation();
      event.dataTransfer.setData(DATA_FORMAT, JSON.stringify(this._file));
    });
  }

  /**
   * Sets rename event to file name field by double click.
   */
  _addRenameHandler() {
    const input = this._listItem.querySelector('.file-name-input');
    if (input) {
      input.focus();
      input.addEventListener('click', (event) => {
        event.stopPropagation();
      });
      input.addEventListener('keydown', (event) => {
        if (event.key === 'Enter' && !!input.value.length) {
          this._onRenameHandler && this._onRenameHandler(this._id, input.value);
        }
      });
      input.addEventListener('blur', () => {
        this._isFileRenaming = false;
      });
    }
  }

  /**
   * Sets new isFileRenaming value and renders new file list item according to this value.
   * @param {boolean} isFileRenaming
   */
  set _isFileRenaming(isFileRenaming) {
    this._isRenaming = isFileRenaming;
    this.render();
  }

  /**
   * Adds handler for single click.
   * @param {Function} handler
   */
  onClick(handler) {
    this._oneClickHandler = handler;
  }

  /**
   * Adds handler for double click.
   * @param {Function} handler
   */
  onRename(handler) {
    this._onRenameHandler = handler;
  }

  /**
   * Adds handlers for click and double click actions.
   */
  _addOnClickHandlers() {
    this.clickCount = 0;
    this._listItem.addEventListener('click', (event) => {
      event.stopPropagation();
      this.clickCount++;
      setTimeout(() => {
        if (this.clickCount === 1) {
          if (this._type === 'folder') {
            this._oneClickHandler && this._oneClickHandler();
          }
        }
        this.clickCount = 0;
      }, 500);
    });
    const fileName = this._listItem.querySelector('.file-name-field');
    fileName.addEventListener('dblclick', () => {
      this._isFileRenaming = true;
    });
  }

  /**
   * Creates a {@code Date} instance.
   *
   * @param {Number} uploadDateTimestamp a file uploading timestamp
   * @return {string} the converted date string into the {@code YYYY/MM/DD} format
   * @private
   */
  _convertDate(uploadDateTimestamp) {
    return new Date(uploadDateTimestamp)
        .toJSON().slice(0, 10).replace(/-/g, '/');
  }

  /**
   * Converts the obtained file size to the understandable value.
   *
   * @param {Number} size a file size in byte form
   * @return {string} a formatted file size
   * @private
   */
  _convertSize(size) {
    let formattedSize;
    const kilobyte = 1024;
    const megabyte = 1048576;
    const gigabyte = 1073741824;
    const kilobytesAmount = size / kilobyte;

    if (size / gigabyte > 1) {
      formattedSize = Math.floor(size / gigabyte) + ' Gb';
    } else if (size / megabyte > 1) {
      formattedSize = Math.floor(size / megabyte) + ' Mb';
    } else if (kilobytesAmount < kilobyte && kilobytesAmount > 1) {
      formattedSize = Math.floor(kilobytesAmount) + ' Kb';
    } else {
      formattedSize = size + ' b';
    }

    return formattedSize;
  }
}
