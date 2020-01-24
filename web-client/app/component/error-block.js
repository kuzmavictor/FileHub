import {Component} from './component.js';

/**
 * Stands for error block, that shows runtime errors to user
 * which can react according to error message.
 */
export class ErrorBlock extends Component {
  /**
   * @param {HTMLElement} parentNode
   * @param {string} errorMessage
   */
  constructor(parentNode, errorMessage) {
    super(parentNode);
    this._errorMessage = errorMessage;
    this.render();
  }

  /**
   * Renders error block object without showing.
   */
  render() {
    this._errorBlock = document.createElement('div');
    this._errorBlock.className = 'alert alert-danger error-block';
    this._errorBlock.innerHTML = `
        <strong>Warning!</strong>
        ${this._errorMessage}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    `;
    this.parentNode.appendChild(this._errorBlock);
    this._addClickHandler();
  }

  /**
   * Adds click handler for close button.
   * @private
   */
  _addClickHandler() {
    this._errorBlock.querySelector('.close').addEventListener('click', (event) => {
      event.stopPropagation();
      this._errorBlock.remove();
    });
  }
}
