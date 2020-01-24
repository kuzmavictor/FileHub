import {Component} from './component.js';

/**
 * Stands for abstract configurable input field.
 */
export class InputField extends Component {
  /**
   * @param {HTMLElement} parentNode
   * @param {string} id
   * @param {string} type
   * @param {string} text
   */
  constructor(parentNode, id, type, text) {
    super(parentNode);
    this._id = id;
    this._text = text;
    this._type = type;

    this.render();
  }

  /**
   * Returns value from input field.
   * @return {string}
   */
  get value() {
    return this._input.value;
  }

  /**
   * Set error message to error field after input field.
   * @param {string} errorMessage
   */
  set errorField(errorMessage) {
    if (!!errorMessage) {
      this._error.innerHTML = `${errorMessage}<br/>`;
      this._inputWrapper.classList.add('has-error');
    } else {
      this._error.innerHTML = ``;
      this._inputWrapper.classList.remove('has-error');
    }
  }

  /**
   * Renders and displays input field.
   */
  render() {
    this._wrapper = document.createElement('div');
    this._wrapper.className = 'form-group form-input';

    this._label = document.createElement('label');
    this._label.htmlFor = this._id;

    this._inputWrapper = document.createElement('div');
    this._inputWrapper.className = 'input-wrapper';
    this._input = document.createElement('input');
    this._input.type = this._type;
    this._input.id = this._id;
    this._input.className = 'form-control';
    this._inputWrapper.appendChild(this._input);

    if (!!this._text) {
      this._label.innerHTML = `${this._text} `;
    }

    const errorWrapper = document.createElement('div');
    errorWrapper.classList.add('col-xs-12');
    this._error = document.createElement('span');
    this._error.className = `error-message`;
    errorWrapper.appendChild(this._error);
    this._wrapper.appendChild(this._label);
    this._wrapper.appendChild(this._inputWrapper);
    this._wrapper.appendChild(errorWrapper);
    this.parentNode.appendChild(this._wrapper);
  }
}
