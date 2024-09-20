import axios from "axios";
import {apiConfig, BASE_URL} from "./apiConfig.ts";

export const signin = async (email: string, password: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/sign-in`, {email, password}, {headers});
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error.');
        }
        throw new Error('Unknown error.');
    }
};

export const signup = async (email: string, password: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/sign-up`, {email, password}, {headers});
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error.');
        }
        throw new Error('Unknown error.');
    }
};
