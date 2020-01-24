import {Component} from './component.js';

/**
 * Stands for simple upload button that can add files to file list.
 */
export class UploadButton extends Component {
  /**
   * @param {HTMLElement} parentNode
   */
  constructor(parentNode) {
    super(parentNode);
    this.render();
  }

  /**
   * Renders upload button object.
   */
  render() {
    this.buttonWrapper = document.createElement('div');
    this.buttonWrapper.className = 'upload-button-wrapper';
    this.buttonWrapper.innerHTML = `
    <label for="hidden-file-input" class="upload-button">Upload</label>
        <input type="file" id="hidden-file-input" multiple>
    `;
    this.parentNode.appendChild(this.buttonWrapper);
  }

  /**
   * Adds event handler to input field and executes it when files are chosen.
   * @param {Function} handler
   */
  onclick(handler) {
    const button = this.parentNode.querySelector('#hidden-file-input');
    button.addEventListener('change', handler);
    button.value = '';
  }

  /**
   * @return {FileList}
   */
  get files() {
    return this.parentNode.querySelector('#hidden-file-input').files;
  }
}
