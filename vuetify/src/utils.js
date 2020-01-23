import axios from 'axios'
//import vuetify from './plugins/vuetify'

export const axiosInstance = axios.create({
  baseURL: 'http://localhost'
});