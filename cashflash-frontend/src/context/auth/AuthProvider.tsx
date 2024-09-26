import React, {ReactNode, useEffect, useState} from 'react';
import {AuthContext, useLanguage} from '../index.ts';
import {getUserPreferences, signin as apiLogin, signout as apiSignout, signup as apiSignup} from "../../services";
import {decodeToken} from "../../utils";

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const {language} = useLanguage();
    const [token, setToken] = useState<string | ''>(localStorage.getItem('token') || '');
    const [isSignedIn, setIsLoggedIn] = useState<boolean>(!!token);

    const handleUserPreferences = async (token: string) => {
        const decoded = decodeToken(token);
        if (decoded) {
            const userId = decoded.userId;
            const email = decoded.email;
            const preferences = await getUserPreferences(userId, email, language, token);
            if (preferences.defaultTheme !== null) {
                localStorage.setItem('theme', preferences.defaultTheme);
                window.dispatchEvent(new CustomEvent('themeChange', {detail: preferences.defaultTheme}));
            }
            if (preferences.defaultLanguage !== null) {
                localStorage.setItem('language', preferences.defaultLanguage);
                window.dispatchEvent(new CustomEvent('languageChange', {detail: preferences.defaultLanguage}));
            }
        }
    };

    const signin = async (email: string, password: string) => {
        const newToken = await apiLogin(email, password, language);
        if (newToken) {
            setToken(newToken);
            setIsLoggedIn(true);
            localStorage.setItem('token', newToken);
            await handleUserPreferences(newToken);
        }
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
        const handleAuthenticationFlow = async () => {
            const searchParams = new URLSearchParams(window.location.search);
            const tokenFromParams = searchParams.get('token');

            if (tokenFromParams && tokenFromParams !== "oauth") {
                setIsLoggedIn(true);
                localStorage.setItem('token', tokenFromParams);
                setToken(tokenFromParams);
                await handleUserPreferences(tokenFromParams);
                setTimeout(() => {
                    const newUrl = `${window.location.origin}`;
                    window.location.replace(newUrl);
                }, 500);
            }

            if (tokenFromParams === "oauth") {
                await apiSignout(language);
                const newUrl = `${window.location.origin}/sign-in`;
                window.location.replace(newUrl);
                setTimeout(() => {
                    window.location.reload();
                }, 500);
            }
        };

        handleAuthenticationFlow().then();
    }, []);

    return (
        <AuthContext.Provider value={{isSignedIn, token, signin, signup, signout}}>
            {children}
        </AuthContext.Provider>
    );
};
