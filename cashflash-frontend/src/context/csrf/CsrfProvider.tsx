import React, {useEffect, useState} from "react";
import {CsrfContext} from "../index";
import {getCsrfToken, setCsrfToken} from "../../services";
import axios from "axios";

export const CsrfProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [csrfToken, setCsrfTokenState] = useState<string | null>(null);

    useEffect(() => {
        const fetchCsrfToken = async () => {
            try {
                const token = await getCsrfToken();
                setCsrfTokenState(token);
                setCsrfToken(token);
            } catch (error) {
                if (axios.isAxiosError(error) && error.response) {
                    throw new Error(error.response.data.errorMessage || 'Unknown error');
                }
                throw new Error('Unknown error');
            }
        };

        fetchCsrfToken().then(r => r);
    }, []);

    return (
        <CsrfContext.Provider value={{csrfToken}}>
            {children}
        </CsrfContext.Provider>
    );
};
