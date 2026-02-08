import axios from 'axios';

const apiUrl = import.meta.env.VITE_BACKEND_URL;
const api = axios.create({
    baseURL: `${apiUrl}`, //adresa a api-urilor
});

//adaugare jwt
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

export default api;