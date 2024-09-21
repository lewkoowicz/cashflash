import axios from 'axios';
import {InvestmentRequest, InvestmentResponse} from "./index.ts";
import {apiConfig, BASE_URL} from "../apiConfig.ts";

export const createInvestment = async (data: InvestmentRequest, language: string): Promise<InvestmentResponse> => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post<InvestmentResponse>(`${BASE_URL}/create`, data, {headers});
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error');
        }
        throw new Error('Unknown error');
    }
};
