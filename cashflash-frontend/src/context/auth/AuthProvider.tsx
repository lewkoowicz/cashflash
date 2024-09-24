import React, {ReactNode, useEffect, useState} from 'react';
import {AuthContext, useLanguage} from '../index.ts';
import {signin as apiLogin, signout as apiSignout, signup as apiSignup} from "../../services";

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const {language} = useLanguage();
    const [token, setToken] = useState<string | ''>(localStorage.getItem('token') || '');
    const [isSignedIn, setIsLoggedIn] = useState<boolean>(!!token);

    const signin = async (email: string, password: string) => {
        const data = await apiLogin(email, password, language);
        localStorage.setItem('token', data.token);
        setToken(data.token);
        setIsLoggedIn(true);
    };

    const signup = async (email: string, password: string) => {
        await apiSignup(email, password, language);
    };

    const signout = async () => {
        await apiSignout(language);
        localStorage.removeItem('token');
        setIsLoggedIn(false);
        const newUrl = `${window.location.origin}/sign-in`;
        window.location.replace(newUrl);
        setTimeout(() => {
            window.location.reload();
        }, 500);
    };

    useEffect(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const tokenFromParams = searchParams.get('token');

        if (tokenFromParams && tokenFromParams !== "oauth") {
            setIsLoggedIn(true);
            localStorage.setItem('token', tokenFromParams);
            setTimeout(() => {
                const newUrl = `${window.location.origin}`;
                window.location.replace(newUrl);
            }, 500);
        }

        if (tokenFromParams === "oauth") {
            apiSignout(language).then(r => r);
            const newUrl = `${window.location.origin}/sign-in`;
            window.location.replace(newUrl);
            setTimeout(() => {
                window.location.reload();
            }, 500);
        }

        const storedToken = localStorage.getItem('token');
        if (storedToken) {
            setToken(storedToken);
        }
    }, []);

    return (
        <AuthContext.Provider value={{isSignedIn, token, signin, signup, signout}}>
            {children}
        </AuthContext.Provider>
    );
};
