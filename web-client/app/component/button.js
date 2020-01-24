import {Component} from './component.js';

/**
 * Stands for abstract configurable button.
 */
export class Button extends Component {
  /**
   * @param {HTMLElement} parentNode
   * @param {string} buttonText
   * @param {string} className
   */
  constructor(parentNode, buttonText, className) {
    super(parentNode);
    this._buttonText = buttonText;
    this._className = className;
    this.render();
  }

  /**
   * Renders and displays button object.
   */
  render() {
    this.button = document.createElement('button');
    this.button.textContent = this._buttonText;
    this.button.className = this._className;
    this.parentNode.appendChild(this.button);
  }

  /**
   * Makes button clickable.
   */
  enable() {
    this.button.disabled = false;
  }

  /**
   * Makes button non-clickable.
   */
  disable() {
    this.button.disabled = true;
  }

  /**
   * Creates event `click` for button with obtained handler function.
   * @param {Function} handler function that is executed when button is clicked.
   */
  onClick(handler) {
    this.button.addEventListener('click', (event) => {
      event.preventDefault();
      event.stopPropagation();
      handler();
    });
  }
}
