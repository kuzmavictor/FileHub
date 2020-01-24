import {Component} from './component.js';

/**
 * Stands for abstract component, that can render some DOM-object.
 */
export class StatefulComponent extends Component {
  /**
   * @param {Store} store
   * @param {HTMLElement} parentNode
   */
  constructor(store, parentNode) {
    super(parentNode);
    this.store = store;
  }
}
