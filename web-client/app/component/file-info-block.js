import {StatefulComponent} from './stateful-component.js';

/**
 * Stands for simple file info block, containing main information about obtained file.
 */
export class FileInfoBlock extends StatefulComponent {
  /**
   * @param {Store} store
   * @param {HTMLElement} parentNode
   */
  constructor(store, parentNode) {
    super(store, parentNode);
    this._initialRender();
  }

  /**
   * Draft rendering of main DOM-elements for file info block.
   * @private
   */
  _initialRender() {
    this._fileInfo = document.createElement('div');
    this._fileInfo.classList.add('pull-right', 'file-info-box');
    this._innerFileInfoBox = document.createElement('div');
    this._innerFileInfoBox.className = 'inner-file-info-box pre-scrollable';

    this._fileInfo.appendChild(this._innerFileInfoBox);
    this.parentNode.append(this._fileInfo);
    this.store.events.subscribe('stateChange.selectedFile', () => {
      this.render();
    });
  }

  /**
   * Renders new file info from selected file places in state.
   */
  render() {
    const selectedFile = this.store.state.selectedFile;
    if (!!selectedFile) {
      this._innerFileInfoBox.innerHTML = `
                <div class="header">
                    
                    <h3><a href="#">${selectedFile.name}</a></h3>
                    <button class="btn btn-default close-button" type="button">
                        <span aria-hidden="true" class="glyphicon glyphicon-remove"></span>
                    </button>
                </div>

                <div class="file-description">
                    <div class="text-description">
                        <dl class="description-fields">
                            <dt>Type</dt>
                            <dd>${selectedFile.type}</dd>
                            <dt>Size</dt>
                            <dd>${(selectedFile.size !== 0) ?
        this._convertSize(selectedFile.size) : ``}</dd>
                            <dt>Location</dt>
                            <dd>file-location</dd>
                            <dt>Owner</dt>
                            <dd>${selectedFile.owner}</dd>
                            <dt>Uploaded</dt>
                            <dd>${this._convertDate(selectedFile.uploadDate)}</dd>
                        </dl>
                    </div>
                </div>`;
      const closeButton = this._innerFileInfoBox.querySelector('.close-button');
      closeButton.addEventListener('click', () => {
        this._close();
        this.store.dispatch('selectFile', undefined);
      });
      this._open();
    } else {
      this._close();
    }
  }

  /**
   * Closes file info block.
   * @private
   */
  _close() {
    this._fileInfo.classList.remove('opened');
  }

  /**
   * Opens file info block.
   * @private
   */
  _open() {
    this._fileInfo.classList.add('opened');
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
   * @return {string} the formatted file size
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
