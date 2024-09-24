import {createContext} from 'react';

interface AuthContextType {
    isSignedIn: boolean;
    token: string | '';
    signin: (email: string, password: string) => Promise<void>;
    signup: (email: string, password: string) => Promise<void>;
    signout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
