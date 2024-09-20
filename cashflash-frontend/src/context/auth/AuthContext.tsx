import {createContext} from 'react';

interface AuthContextType {
    isSignedIn: boolean;
    token: string | '';
    role: string | '';
    email: string | '';
    signin: (email: string, password: string) => Promise<void>;
    signup: (email: string, password: string) => Promise<void>;
    signout: () => void;
    signedInWithGoogle: boolean;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
