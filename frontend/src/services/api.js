import axios from 'axios';
import { getToken } from './auth';

const api = axios.create({
  baseURL: 'http://localhost:8080'
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers = config.headers ?? {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export async function login(username, password) {
  const res = await api.post('/api/auth/login', { username, password });
  return res.data;
}

export async function fetchProducts() {
  const res = await api.get('/api/products');
  return res.data;
}

export default api;

