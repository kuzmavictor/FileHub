import {Application} from '../app/component/application.js';
import {FormValidator} from '../app/service/form-validator.js';

QUnit.module('Password tests', {
  beforeEach: function() {
    const root = document.getElementById('qunit-fixture');
    new Application(root);
  },
});

QUnit.test('Test password input.', (assert) => {
  const password = document.getElementById('password');
  const secondPassword = document.getElementById('second-password');
  password.value = '12343ggGG';
  secondPassword.value = '12343ggGG';
  assert.equal(FormValidator.validatePassword(password.value, secondPassword.value), undefined,
      'Should complete if passwords are equal and pass the validation.');
});

QUnit.test('Test password input with wrong value.', (assert) => {
  const password = document.getElementById('password');
  const secondPassword = document.getElementById('second-password');
  password.value = '12343gg';
  secondPassword.value = '12343gg';

  const submitButton = document.getElementById('register');
  const machineEvent = new Event('click', {bubbles: true});
  submitButton.dispatchEvent(machineEvent);

  assert.equal(FormValidator.validatePassword(password.value, secondPassword.value),
      'Password must contain Upper and Lowercase letters and numbers!',
      'Should complete if passwords dont pass the validation.');

  assert.equal(document.getElementById('error-password-message').innerHTML,
      `Password must contain Upper and Lowercase letters and numbers!<br>`,
      'Should create error message block inside password node.');
});

QUnit.test('Test non-equal passwords input.', (assert) => {
  const password = document.getElementById('password');
  const secondPassword = document.getElementById('second-password');
  password.value = '12343ggG';
  secondPassword.value = '12343ggGG';

  const submitButton = document.getElementById('register');
  const machineEvent = new Event('click', {bubbles: true});
  submitButton.dispatchEvent(machineEvent);

  assert.equal(FormValidator.validatePassword(password.value, secondPassword.value),
      'Passwords are not equal!',
      'Should complete if passwords are not equal.');

  assert.equal(document.getElementById('error-password-message').innerHTML,
      `Passwords are not equal!<br>`,
      'Should create error message block inside password node.');
});
