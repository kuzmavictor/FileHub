import {Router} from './router.js';
import {RegistrationController} from '../controller/registration-controller.js';
import {LoginController} from '../controller/login-controller.js';
import {Store} from '../service/store.js';
import state from '../service/state.js';
import {FileListController} from '../controller/file-list-controller.js';
import {actions} from '../service/actions.js';
import {mutations} from '../service/mutations.js';
import {ServiceFactory} from '../service/service-factory.js';

/**
 * Stands for entire application.
 */
export class Application {
  /**
   * @param {HTMLElement} root
   */
  constructor(root) {
    this._root = root;
    this.render();
  }

  /**
   * Method, that renders and displays objects contained in application.
   */
  render() {
    const routes = {
      '/login': LoginController,
      '/registration': RegistrationController,
      '/file-list': FileListController,
    };

    const serverUrl = 'http://localhost:4567';
    const serviceFactory = new ServiceFactory(serverUrl);
    const store = new Store(state, mutations, actions, serviceFactory);
    const router = new Router(routes);
    router.addRedirect('/', '/login');
    serviceFactory.onPageNotFound(() => {
      router.redirect('#/page404');
    });

    router.onRouteChanged((Controller) => {
      this._root.innerHTML = '';
      new Controller(this._root, router, store);
    });
  }
}
