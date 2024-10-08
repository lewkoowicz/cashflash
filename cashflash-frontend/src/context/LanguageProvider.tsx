import React, {createContext, ReactNode, useContext, useEffect, useState} from 'react';

interface LanguageContextType {
    language: 'pl' | 'en';
    toggleLanguage: () => void;
}

const LanguageContext = createContext<LanguageContextType | undefined>(undefined);

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

export const useLanguage = () => {
    const context = useContext(LanguageContext);
    if (context === undefined) {
        throw new Error('useLanguage must be used within a LanguageProvider');
    }
    return context;
};
