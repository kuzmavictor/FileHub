/**
 * Regular expression, that checks if the some value matches the given pattern.
 * @type {RegExp}
 */
const REG_EXP_FOR_LOGIN_AND_PASSWORDS = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{6,255}$/;

/**
 * Constant image size for validating images.
 * @type {number}
 */
const IMAGE_SIZE = 500 * 1024;

/**
 * Image allowed formats.
 * @type {string[]}
 */
const IMAGE_FORMATS = ['image/jpeg', 'image/png'];

/**
 * Contains utility-methods for validating fields according to specific patterns.
 */
export class FormValidator {
  /**
   * Validates image file by size and file formats.
   * @param {File} file
   * @return {string} error message.
   */
  static validateAvatar(file) {
    if (file) {
      if (!IMAGE_FORMATS.includes(file.type)) {
        return 'Wrong file format!';
      }

      if (file.size > IMAGE_SIZE) {
        return 'Image too big!';
      }
    } else {
      return 'No file chosen!';
    }
  }

  /**
   * Validates login value by pattern.
   * @param {string} loginValue
   * @return {string}
   */
  static validateLogin(loginValue) {
    if (!this.test(loginValue)) {
      return 'Wrong login input!';
    }
  }

  /**
   * Validates password value by pattern.
   * @param {string} password
   * @return {string}
   */
  static validatePassword(password) {
    if (!this.test(password)) {
      return 'Password must contain Upper and Lowercase letters and numbers!';
    }
  }

  /**
   * Validates password values by pattern.
   * @param {string} firstPassword
   * @param {string} secondPassword
   * @return {string}
   */
  static validatePasswords(firstPassword, secondPassword) {
    if (firstPassword !== secondPassword) {
      return 'Passwords are not equal!';
    }

    if (!(this.test(firstPassword) && this.test(secondPassword))) {
      return 'Password must contain Upper and Lowercase letters and numbers!';
    }
  }

  /**
   * Tests value for pattern matching.
   * @param {string} value
   * @return {boolean}
   */
  static test(value) {
    return REG_EXP_FOR_LOGIN_AND_PASSWORDS.test(value);
  }
}
