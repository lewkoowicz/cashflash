import axios from "axios";
import {apiConfig, BASE_URL, PasswordChange} from "./index.ts";

export const signin = async (email: string, password: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/sign-in`, {email, password}, {headers});
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
};

export const signup = async (email: string, password: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/sign-up`, {email, password}, {headers});
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
};

export const signout = async (language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/sign-out`, {}, {headers});
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
};

export const changePassword = async (data: PasswordChange, language: string, token: string, email: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post(`${BASE_URL}/change-password?email=${email}`,
            data,
            {headers}
        );
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
}

export const deleteAccount = async (email: string, deleteString: string, language: string, token: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post(`${BASE_URL}/delete-account?email=${email}&delete=${deleteString}`,
            {},
            {headers}
        );
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
}

export const forgotPasword = async (email: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/forgot-password`,
            {email},
            {headers}
        );
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
}

export const resetPassword = async (token: string, password: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/reset-password`,
            {token, password},
            {headers}
        );
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage);
        }
        throw new Error('Unknown error');
    }
}
