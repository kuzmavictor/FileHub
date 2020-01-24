import {Application} from '../app/component/application.js';
import {FormValidator} from '../app/service/form-validator.js';

QUnit.module('Login tests', {
  beforeEach: function() {
    const root = document.getElementById('qunit-fixture');
    new Application(root);
  },
});

QUnit.test('Test login input.', (assert) => {
  const login = document.getElementById('login');
  login.value = '12343ggGG';
  assert.equal(FormValidator.validateLogin(login.value), undefined,
      'Should complete if login passes the validation.');
});

QUnit.test('Test login input with wrong value.', (assert) => {
  const login = document.getElementById('login');
  login.value = '12343gg';

  const submitButton = document.getElementById('register');
  const machineEvent = new Event('click', {bubbles: true});
  submitButton.dispatchEvent(machineEvent);

  assert.equal(FormValidator.validateLogin(login.value), 'Wrong login input!',
      'Should complete if login does not pass the validation.');
  assert.equal(document.getElementById('error-login-message').innerHTML,
      `Wrong login input!<br>`, 'Should create error message block inside login node.');
});
