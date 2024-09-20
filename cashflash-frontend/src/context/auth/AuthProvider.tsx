import React, {ReactNode, useEffect, useState} from 'react';
import {AuthContext, useLanguage} from '../index.ts';
import {signin as apiLogin, signup as apiSignup} from "../../services";

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [token, setToken] = useState<string | ''>(localStorage.getItem('token') || '');
    const [role, setRole] = useState<string | ''>(localStorage.getItem('role') || '');
    const [email, setEmail] = useState<string | ''>(localStorage.getItem('email') || '');
    const [isSignedIn, setIsLoggedIn] = useState<boolean>(!!role);
    const [signedInWithGoogle, setSignedInWithGoogle] = useState<boolean>(false);

    const {language} = useLanguage();

    const signin = async (email: string, password: string) => {
        const data = await apiLogin(email, password, language);
        localStorage.setItem('token', data.token);
        localStorage.setItem('role', data.role);
        localStorage.setItem('email', data.email);
        setToken(data.token);
        setEmail(data.email);
        setRole(data.role);
        setIsLoggedIn(true);
    };

    const signup = async (email: string, password: string) => {
        await apiSignup(email, password, language);
    };

    const signout = async () => {
        await fetch('http://localhost:8080/api/sign-out', {
            method: 'POST',
            credentials: 'include',
        });
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('email');
        setIsLoggedIn(false);
        const newUrl = `${window.location.origin}/sign-in`;
        window.location.replace(newUrl);
        setTimeout(() => {
            window.location.reload();
        }, 500);
    };

    useEffect(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const emailFromParams = searchParams.get('email');
        const roleFromParams = searchParams.get('role');

        if (emailFromParams && roleFromParams) {
            setEmail(emailFromParams);
            setRole(roleFromParams);
            localStorage.setItem('email', emailFromParams);
            localStorage.setItem('role', roleFromParams);
            setIsLoggedIn(true);
            setSignedInWithGoogle(true);
            setTimeout(() => {
                const newUrl = `${window.location.origin}/bookings`;
                window.location.replace(newUrl);
            }, 500);
        }

        const storedRole = localStorage.getItem('role');
        if (storedRole) {
            setRole(storedRole);
        }
    }, []);

    return (
        <AuthContext.Provider value={{isSignedIn, token, role, email, signin, signup, signout, signedInWithGoogle}}>
            {children}
        </AuthContext.Provider>
    );
};
