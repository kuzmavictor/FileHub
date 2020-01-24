import {Component} from './component.js';

/**
 * Stands for image component, creates DIV-element and IMAGE in it.
 */
export class Image extends Component {
  /**
   * @param {HTMLElement} parentNode
   * @param {FileList} inputFiles
   */
  constructor(parentNode, inputFiles) {
    super(parentNode);
    this.inputFiles = inputFiles;
    this.render();
  }

  /**
   * Renders and displays image.
   */
  render() {
    const image = document.getElementsByClassName('registration-preview-image')[0];
    const fileType = this.inputFiles[0].type;

    if (!!image) {
      if (fileType === 'image/png' || fileType === 'image/jpeg') {
        image.src = window.URL.createObjectURL(this.inputFiles[0]);
      } else {
        this.parentNode.removeChild(image.parentNode);
      }
    } else if (fileType === 'image/png' || fileType === 'image/jpeg') {
      this.parentNode.innerHTML = `
          <div class="image">
              <img src="${window.URL.createObjectURL(this.inputFiles[0])}" 
                class="registration-preview-image">
          </div>
      `;
    }
  }
}
