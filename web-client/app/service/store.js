import PubSub from './pubsub.js';

/**
 * Stands for store, that contains current application state, actions and mutations.
 */
export class Store {
  /**
   * @param {Object} state
   * @param {Object} mutations
   * @param {Object} actions
   * @param {ServiceFactory} serviceFactory
   */
  constructor(state, mutations, actions, serviceFactory) {
    this.events = new PubSub();
    this.state = new Proxy(state, {
      set: (state, key, value) => {
        state[key] = value;
        this.events.publish('stateChange.' + key, this.state);
        return true;
      },
    });
    this._serviceFactory = serviceFactory;
    this.actions = actions;
    this.mutations = mutations;
  }

  /**
   * @param {string} actionName
   * @param {Object} dataArgs
   * @return {Promise}
   */
  dispatch(actionName, ...dataArgs) {
    return this.actions[actionName](this)(...dataArgs);
  }

  /**
   * @param {string} mutationName
   * @param {number} dataArgs
   */
  commit(mutationName, ...dataArgs) {
    this.mutations[mutationName](this.state, ...dataArgs);
  }

  /**
   * @return {ServiceFactory}
   */
  get serviceFactory() {
    return this._serviceFactory;
  }
}
