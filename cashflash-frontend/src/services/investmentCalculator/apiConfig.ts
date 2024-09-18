export const BASE_URL = 'http://localhost:8080/api';

export const apiConfig = {
    getHeaders: () => {
        return {
            'Content-Type': 'application/json'
        };
    }
};
