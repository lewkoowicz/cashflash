export const BASE_URL = 'http://localhost:8080/api';

export const apiConfig = {
    getHeaders: (language: string, token?: string) => {
        return {
            'Accept-Language': language,
            'Authorization': token ? `Bearer ${token}` : '',
            'Content-Type': 'application/json'
        };
    }
};
