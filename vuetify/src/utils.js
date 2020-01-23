import axios from 'axios'
//import vuetify from './plugins/vuetify'
import vuetify from './plugins/vuetify';

export const axiosInstance = axios.create({
  baseURL: 'http://localhost'
});

axiosInstance.interceptors.request.use(function (config) {
  var lang = vuetify.framework.lang.current
  config.headers['Accept-Language'] = lang
  return config;
}, function (error) {
  // Do something with request error
  return Promise.reject(error);
});