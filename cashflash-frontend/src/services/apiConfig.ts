import axios from "axios";

export const BASE_URL = 'http://localhost:8080/api';

let csrfToken: string | null = null;

export const setCsrfToken = (token: string) => {
    csrfToken = token;
};

export const apiConfig = {
    getHeaders: (language: string, token?: string) => {
        return {
            'Accept-Language': language,
            'Authorization': token ? `Bearer ${token}` : '',
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken || ''
        };
    }
};

export const getCsrfToken = async () => {
    try {
        const response = await axios.get(`${BASE_URL}/csrf-token`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
};

axios.defaults.withCredentials = true;
