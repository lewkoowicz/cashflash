import React, {useCallback, useEffect, useState} from "react";
import {CsrfContext} from "../index";
import {getCsrfToken, setCsrfToken} from "../../services";
import axios from "axios";

export const CsrfProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [csrfToken, setCsrfTokenState] = useState<string | null>(null);

    const fetchCsrfToken = useCallback(async () => {
        try {
            const token = await getCsrfToken();
            setCsrfTokenState(token);
            setCsrfToken(token);
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                throw new Error(error.response.data.errorMessage);
            }
            throw new Error('Unknown error');
        }
    }, []);

    useEffect(() => {
        fetchCsrfToken().then(r => r);

        const refreshInterval = setInterval(fetchCsrfToken, 15 * 60 * 1000);

        return () => clearInterval(refreshInterval);
    }, [fetchCsrfToken]);

    return (
        <CsrfContext.Provider value={{csrfToken}}>
            {children}
        </CsrfContext.Provider>
    );
};
