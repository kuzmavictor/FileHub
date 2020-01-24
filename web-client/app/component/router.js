import {Page404} from './page-404.js';

/**
 * Responsible for redirecting pages and the routing between them.
 * Creates an event that, when clicking on the link, generates the given page.
 *
 */
export class Router {
  /**
   * @param {Object} routingConfig
   */
  constructor(routingConfig) {
    this.routingConfig = routingConfig;
    this.redirects = [];
    window.addEventListener('hashchange', () => this._routeChanged());
  }

  /**
   * Redirects one poge to target page.
   * @param {string} target
   * @private
   */
  static _redirectTo(target) {
    window.location.hash = '#' + target;
  }

  /**
   * Obtains URL of result page.
   * @return {string}
   */
  static getURL() {
    const hash = window.location.hash || '#/';
    return hash.slice(1).toLocaleLowerCase();
  }

  /**
   * Handler for `hashchange` event.
   */
  _routeChanged() {
    const URL = Router.getURL();
    const redirect = this.redirects.find((redirect) => redirect.from === URL);

    if (!!redirect) {
      Router._redirectTo(redirect.to);
      return;
    }

    let page = this.routingConfig[URL];
    if (!page) {
      page = Page404;
    }
    this.createPageFunction(page);
  }

  /**
   * Sets event listener to page event "hashchange"
   * @param {Function} createPageFunction
   */
  onRouteChanged(createPageFunction) {
    this.createPageFunction = createPageFunction;
    this._routeChanged();
  }

  /**
   * Add redirect from one URL (from) to another URL (to).
   * @param {string} from
   * @param {string} to
   */
  addRedirect(from, to) {
    this.redirects.push({
      from: from,
      to: to,
    });
  }

  /**
   * @param {string} URL
   */
  redirect(URL) {
    window.location.href = URL;
  }
}
