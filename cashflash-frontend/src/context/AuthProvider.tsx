import React, {createContext, ReactNode, useContext, useEffect, useState} from 'react';
import {useLanguage} from './index.ts';
import {getUserPreferences, signin as apiLogin, signout as apiSignout, signup as apiSignup} from "../services";
import {decodeToken} from "../utils";

interface AuthContextType {
    isSignedIn: boolean;
    token: string | '';
    signin: (email: string, password: string) => Promise<void>;
    signup: (email: string, password: string) => Promise<void>;
    signout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

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

    const checkTokenExpiration = () => {
        const currentToken = localStorage.getItem('token');
        if (currentToken) {
            const decodedToken = decodeToken(currentToken);
            if (decodedToken && decodedToken.exp) {
                const currentTime = Math.floor(Date.now() / 1000);
                if (decodedToken.exp < currentTime) {
                    signout().then();
                }
            }
        }
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

        checkTokenExpiration();

        const intervalId = setInterval(checkTokenExpiration, 60000);

        return () => {
            clearInterval(intervalId);
        };
    }, []);

    return (
        <AuthContext.Provider value={{isSignedIn, token, signin, signup, signout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
