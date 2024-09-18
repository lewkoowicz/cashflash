import axios from 'axios';
import {BASE_URL, apiConfig, InvestmentRequest, InvestmentResponse} from "./index.ts";

export const createInvestment = async (data: InvestmentRequest): Promise<InvestmentResponse> => {
    const headers = apiConfig.getHeaders();
    try {
        const response = await axios.post<InvestmentResponse>(`${BASE_URL}/create`, data, {
            headers
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error occurred');
        }
        throw new Error('Network error');
    }
};
