import Axios from "axios";

const API_URL = process.env.REACT_APP_API_URL;
Axios.defaults.baseURL = API_URL;

export class HttpService {
  _axios = Axios.create();

  addRequestInterceptor = (onFulfilled, onRejected) => {
    this._axios.interceptors.request.use(onFulfilled, onRejected);
  };

  addResponseInterceptor = (onFulfilled, onRejected) => {
    this._axios.interceptors.response.use(onFulfilled, onRejected);
  };

  get = async (url, headers = {}) => await this.request(this.getOptionsConfig("get", url, null, headers));

  post = async (url, data, headers) => await this.request(this.getOptionsConfig("post", url, data, headers));

  put = async (url, data, headers) => await this.request(this.getOptionsConfig("put", url, data, headers));

  patch = async (url, data, headers) => await this.request(this.getOptionsConfig("patch", url, data, headers));

  delete = async (url) => await this.request(this.getOptionsConfig("delete", url));

  getOptionsConfig = (method, url, data, headers = {}) => {
    const options = {
      method,
      url,
      data,
      headers: { "Content-Type": "application/vnd.api+json", "Accept": "application/vnd.api+json", 'Access-Control-Allow-Credentials': true, ...headers }
    }
    return options;
  };

  // request(options) {
  //   return new Promise((resolve, reject) => {
  //     this._axios
  //       .request(options)
  //       .then((res) => resolve(res.data))
  //       .catch((ex) => reject(ex.response.data));
  //   });
  // }
  request(options) {
    return new Promise((resolve, reject) => {
      this._axios
        .request(options)
        .then((res) => resolve(res))
        .catch((ex) => { console.log(ex); reject(ex) });
    });
  }
}

export default new HttpService();
