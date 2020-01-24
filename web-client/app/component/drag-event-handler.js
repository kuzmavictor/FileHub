/**
 * Changes styles of target node when user drags or drops files.
 */
export class DragEventHandler {
  /**
   */
  constructor() {
    this._enterCount = 0;
  }

  /**
   * Adds `drag-entered` class to obtained node on `enter`.
   * @param {HTMLElement} node
   */
  _addEnterClass(node) {
    this._enterCount++;
    node.classList.add('drag-entered');
  }

  /**
   * Removes `drag-entered` class from obtained node on `leave`.
   * @param {HTMLElement} node
   */
  _removeEnterClass(node) {
    this._enterCount--;
    if (this._enterCount === 0) {
      node.classList.remove('drag-entered');
    }
  }

  /**
   * Changes styles for node according to needed event.
   * @param {HTMLElement} node
   */
  handleDragStylesChanges(node) {
    node.addEventListener('dragenter', (event) => {
      this.preventDefaultEventBehaviour(event);
      this._addEnterClass(node);
    });

    node.addEventListener('dragover', (event) => {
      this.preventDefaultEventBehaviour(event);
    });
    node.addEventListener('dragleave', (event) => {
      this._removeEnterClass(node);
      this.preventDefaultEventBehaviour(event);
    });
    node.addEventListener('drop', (event) => {
      this.preventDefaultEventBehaviour(event);
      this._enterCount--;
      node.classList.remove('drag-entered');
    });
  }

  /**
   * Prevents default event behaviour and stops propagation.
   * @param {Event} event
   */
  preventDefaultEventBehaviour(event) {
    event.stopPropagation();
    event.preventDefault();
  }
}
