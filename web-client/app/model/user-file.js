/**
 * Contains fields required to display file information.
 */
export class UserFile {
  /**
   * @param {string} name
   * @param {string} size
   * @param {string} uploadDate
   * @param {string} owner
   */
  constructor(name, size, uploadDate, owner) {
    this._name = name;
    this._size = size;
    this._uploadDate = uploadDate;
    this._owner = owner;
  }

  /**
   * Returns file name.
   * @return {string}
   */
  get name() {
    return this._name;
  }

  /**
   * Sets file name value.
   * @param {string} value
   */
  set name(value) {
    this._name = value;
  }

  /**
   * Returns file size.
   * @return {string}
   */
  get size() {
    return this._size;
  }

  /**
   * Sets file size value.
   * @param {string} value
   */
  set size(value) {
    this._size = value;
  }

  /**
   * Returns file upload date.
   * @return {string}
   */
  get uploadDate() {
    return this._uploadDate;
  }

  /**
   * Sets file upload date value.
   * @param {string} value
   */
  set uploadDate(value) {
    this._uploadDate = value;
  }

  /**
   * Returns file owner.
   * @return {string}
   */
  get owner() {
    return this._owner;
  }

  /**
   * Sets file owner name.
   * @param {string} name
   */
  set owner(name) {
    this._owner = name;
  }
}
