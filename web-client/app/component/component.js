/**
 * Stands for abstract component object.
 */
export class Component {
  /**
   *
   * @param {HTMLElement} parentNode
   */
  constructor(parentNode) {
    this.parentNode = parentNode;
  }

  /**
   * Renders child nodes.
   */
  render() {
    throw new Error('Should be implemented!');
  }
}
