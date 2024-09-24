import axios, {isAxiosError} from "axios";
import {apiConfig, BASE_URL, UserPreferences} from './index';

export const getUserPreferences = async (userId: number, email: string, language: string, token: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.get<UserPreferences>(
            `${BASE_URL}/get-user-preferences?userId=${userId}&email=${email}`, {
                headers
            });
        return response.data;
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
}

export const setDefaultTheme = async (userId: number, theme: string, email: string, language: string, token: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post(
            `${BASE_URL}/set-default-theme?userId=${userId}&theme=${theme}&email=${email}`,
            {},
            {headers}
        );
        return response.data;
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
}

export const setDefaultLanguage = async (userId: number, languageApi: string, email: string, language: string, token: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post(
            `${BASE_URL}/set-default-theme?userId=${userId}&language=${languageApi}&email=${email}`, {
                headers
            });
        return response.data;
    } catch (error) {
        if (isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
}
