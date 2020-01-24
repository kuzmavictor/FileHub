import {Component} from './component.js';
import Popper from '../../node_modules/popper.js/dist/esm/popper.js';

/**
 * Stands for `popup` element, that contains some information.
 */
export class PopperComponent extends Component {
  /**
   * @param {HTMLElement} parentNode
   */
  constructor(parentNode) {
    super(parentNode);
    this.render();
  }

  /**
   * Renders popper component
   */
  render() {
    this.popperElement = document.createElement('div');
    this.popperElement.className = 'popover bottom';
    this.popperElement.innerHTML = `
      <h3 class="popover-title">Share link</h3>
      <div class="popover-content">
        <input class="input-field form-control">
      </div>`;
    this.popper = new Popper(this.parentNode, this.popperElement, {
      placement: 'bottom-start',
    });
    this.parentNode.appendChild(this.popperElement);
  }

  /**
   * Shows popup with obtained text on given parent node.
   * @param {string} text
   */
  changePopupText(text) {
    const inputField = this.popperElement.querySelector('.input-field');
    inputField.value = text;
    inputField.readOnly = true;
    inputField.select();
    inputField.focus();

    inputField.addEventListener('blur', () => {
      this.popperElement.remove();
    });
    this.popper.update();
  }
}
