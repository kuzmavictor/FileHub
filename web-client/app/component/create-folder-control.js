import {Component} from './component.js';

/**
 * Creates create folder button, which can be replaced by input after click.
 */
export class CreateFolderControl extends Component {
  /**
   * @param {HTMLElement} parentNode
   */
  constructor(parentNode) {
    super(parentNode);
    this._initialRender();
  }

  /**
   * Initial render of create folder button.
   * @private
   */
  _initialRender() {
    this._buttonWrapper = document.createElement('div');
    this._buttonWrapper.className = `button-wrapper`;
    this.parentNode.appendChild(this._buttonWrapper);
    this._createFolderError = document.createElement('div');
    this._createFolderError.className = 'p-3 mb-2 bg-danger';
    this.parentNode.appendChild(this._createFolderError);
    this.render();
  }

  /**
   * Renders button for creating folder, if it is creating,
   * it renders input field for entering folder name.
   */
  render() {
    this._buttonWrapper.innerHTML = `
    ${(this._isFolderCreating) ? `<input class="form-control folder-input" 
        placeholder="Enter folder name">` :
      `<button class="create-button">Create folder</button>`}
    `;
    if (!this._isFolderCreating) {
      this._addButtonClickHandler();
    }
  }

  /**
   * Adds handler for click event.
   * @private
   */
  _addButtonClickHandler() {
    const button = this.parentNode.querySelector('.create-button');
    button.addEventListener('click', (event) => {
      event.stopPropagation();
      this.isFolderCreating = true;

      const input = this.parentNode.querySelector('.folder-input');
      input.focus();
      input.addEventListener('keydown', (event) => {
        if (event.key === 'Enter' && !!input.value.length) {
          this._folderCreateHandler(input.value);
        }
      });
      input.addEventListener('blur', () => {
        this.hasError(false);
        this.isFolderCreating = false;
      });
    });
  }

  /**
   * Adds handler for folder name input field.
   * @param {Function} handler
   */
  onFolderCreate(handler) {
    this._folderCreateHandler = handler;
  }

  /**
   * Sets folder creating status.
   * @param {boolean} isCreating
   */
  set isFolderCreating(isCreating) {
    this._isFolderCreating = isCreating;
    this.render();
  }

  /**
   * Sets folder creating error status.
   * @param {boolean} flag
   */
  hasError(flag) {
    if (flag) {
      this._buttonWrapper.classList.add('has-error');
    } else {
      this._buttonWrapper.classList.remove('has-error');
    }
  }
}
