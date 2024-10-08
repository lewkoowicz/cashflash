import {createContext, ReactNode, useContext, useEffect, useState} from "react";

interface ThemeContextType {
    theme: string;
    toggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const ThemeProvider = ({children}: { children: ReactNode }) => {
    const [theme, setTheme] = useState<string>(() => {
        return localStorage.getItem('theme') || 'dark';
    });

    useEffect(() => {
        localStorage.setItem('theme', theme);

        const handleThemeChange = (event: CustomEvent) => {
            setTheme(event.detail);
        };

        window.addEventListener('themeChange', handleThemeChange as EventListener);

        return () => {
            window.removeEventListener('themeChange', handleThemeChange as EventListener);
        };
    }, [theme]);

    const toggleTheme = () => {
        const newTheme = theme === 'light' ? 'dark' : 'light';
        setTheme(newTheme);
        localStorage.setItem('theme', newTheme);
    };

    return (
        <ThemeContext.Provider value={{theme, toggleTheme}}>
            <div data-theme={theme}>
                {children}
            </div>
        </ThemeContext.Provider>
    );
};

export const useTheme = () => {
    const context = useContext(ThemeContext);
    if (context === undefined) {
        throw new Error('useTheme must be used within a ThemeProvider');
    }
    return context;
};
