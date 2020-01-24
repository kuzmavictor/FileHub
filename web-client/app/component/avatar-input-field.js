import {Image} from './image.js';
import {Component} from './component.js';

/**
 * Stands for configurable avatar input field.
 */
export class AvatarInputField extends Component {
  /**
   * @param {HTMLElement} parentNode
   */
  constructor(parentNode) {
    super(parentNode);
    this.render();
  }

  /**
   * Renders and displays avatar input field.
   */
  render() {
    this._wrapper = document.createElement('div');
    this._wrapper.className = 'row file-input-block';
    this._wrapper.innerHTML = `
        <label class="file-input" for="hidden-file-input">Select avatar</label>
        <input type="file" id="hidden-file-input" accept="image/*">
    `;

    this._filePreviewBlock = document.createElement('div');
    this._filePreviewBlock.className = 'file-preview';
    this._filePreviewBlock.innerHTML = `
        <img src="img/default-avatar.png" alt="Default avatar">
    `;

    const errorWrapper = document.createElement('div');
    errorWrapper.classList.add('col-xs-12');
    this._error = document.createElement('span');
    this._error.className = `error-message`;
    errorWrapper.appendChild(this._error);

    this._wrapper.appendChild(this._filePreviewBlock);
    this._wrapper.appendChild(errorWrapper);
    this.parentNode.appendChild(this._wrapper);
  }

  /**
   * Creates the event `change` for input field.
   *
   * @param {Function} handler function that is executed when field is changed.
   */
  onChange(handler) {
    const element = this._wrapper.querySelector('#hidden-file-input');
    element.addEventListener('change', () => {
      const reader = new FileReader();

      reader.addEventListener('load', () => {
        this._avatarString = reader.result;
      });
      reader.readAsBinaryString(element.files[0]);

      handler();
    });
  }

  /**
   * Set error message to error field after input field.
   * @param {string} errorMessage
   */
  set errorField(errorMessage) {
    const label = this._wrapper.querySelector('.file-input');
    if (errorMessage) {
      this._error.innerHTML = `${errorMessage}<br/>`;
      label.classList.add('has-error');
    } else {
      this._error.innerHTML = ``;
      label.classList.remove('has-error');
    }
  }

  /**
   * Returns list of files.
   * @return {FileList}
   */
  get inputFiles() {
    return this._wrapper.querySelector('#hidden-file-input').files;
  }

  /**
   * Displays selected image.
   */
  displayImage() {
    new Image(this._filePreviewBlock, this.inputFiles);
  }

  /**
   * Obtains the avatar contents in the string form.
   */
  get avatarContent() {
    return this._avatarString;
  }
}
