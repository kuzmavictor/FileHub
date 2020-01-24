import {Component} from './component.js';

/**
 * Error page, which appears when User enters incorrect URL.
 */
export class Page404 extends Component {
  /**
   * @param {HTMLElement} parentNode
   */
  constructor(parentNode) {
    super(parentNode);
    this.render();
  }

  /**
   * Renders and displays page with error code `404`.
   */
  render() {
    document.title = 'Page 404';
    const errorMessage = document.createElement('span');
    errorMessage.innerHTML = `
        <div class="page-404">
            <div class="page-404-text">
                <h1>Error 404</h1>
                <h2>Page not found.</h2>
            </div>
            <a href = "#/login"><button class="page-404-button">Login page</button></a>
        </div> 
    `;
    this.parentNode.appendChild(errorMessage);
  }
}
