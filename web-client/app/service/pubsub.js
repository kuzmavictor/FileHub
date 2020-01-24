/**
 * Publishes and subscribe to event bus.
 */
export default class PubSub {
  /**
   * Creates PubSub instance with empty `events` object.
   */
  constructor() {
    this.events = {};
  }

  /**
   * Adds callback function to `event bus`.
   * @param {string} event
   * @param {Function} callback
   * @return {number}
   */
  subscribe(event, callback) {
    if (!this.events.hasOwnProperty(event)) {
      this.events[event] = [];
    }
    return this.events[event].push(callback);
  }

  /**
   * Executes callback function according to event.
   * @param {string} event
   * @param {number} data
   * @return {[]}
   */
  publish(event, data) {
    if (!this.events.hasOwnProperty(event)) {
      return [];
    }
    return this.events[event].map((callback) => callback(data));
  }
}
