import React, {ReactNode, useEffect, useState} from 'react';
import {LanguageContext} from './LanguageContext';

interface LanguageProviderProps {
    children: ReactNode;
}

export const LanguageProvider: React.FC<LanguageProviderProps> = ({ children }) => {
    const [language, setLanguage] = useState<'pl' | 'en'>(() => {
        const savedLanguage = localStorage.getItem('language');
        return (savedLanguage === 'pl' || savedLanguage === 'en') ? savedLanguage : 'pl';
    });

    useEffect(() => {
        localStorage.setItem('language', language);
    }, [language]);

    const toggleLanguage = () => {
        setLanguage(prevLanguage => (prevLanguage === 'pl' ? 'en' : 'pl'));
    };

    return (
        <LanguageContext.Provider value={{ language, toggleLanguage }}>
            {children}
        </LanguageContext.Provider>
    );
};
