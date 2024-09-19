import {createContext} from 'react';

interface LanguageContextType {
    language: 'pl' | 'en';
    toggleLanguage: () => void;
}

export const LanguageContext = createContext<LanguageContextType | undefined>(undefined);
