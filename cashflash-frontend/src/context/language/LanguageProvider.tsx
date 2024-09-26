import React, {ReactNode, useEffect, useState} from 'react';
import {LanguageContext} from './LanguageContext';

interface LanguageProviderProps {
    children: ReactNode;
}

export const LanguageProvider: React.FC<LanguageProviderProps> = ({children}) => {
    const [language, setLanguage] = useState<'pl' | 'en'>(() => {
        const savedLanguage = localStorage.getItem('language');
        return (savedLanguage === 'pl' || savedLanguage === 'en') ? savedLanguage : 'pl';
    });

    useEffect(() => {
        localStorage.setItem('language', language);

        const handleLanguageChange = (event: CustomEvent) => {
            const newLanguage = event.detail;
            if (newLanguage === 'pl' || newLanguage === 'en') {
                setLanguage(newLanguage);
            }
        };

        window.addEventListener('languageChange', handleLanguageChange as EventListener);

        return () => {
            window.removeEventListener('languageChange', handleLanguageChange as EventListener);
        };
    }, [language]);

    const toggleLanguage = () => {
        setLanguage(prevLanguage => (prevLanguage === 'pl' ? 'en' : 'pl'));
    };

    return (
        <LanguageContext.Provider value={{language, toggleLanguage}}>
            {children}
        </LanguageContext.Provider>
    );
};
