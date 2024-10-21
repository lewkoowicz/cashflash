import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {InvestmentCalculatorForm, InvestmentCalculatorResults} from "./components/investmentCalculator";
import {Navbar} from "./layouts";
import {
    Cashflash,
    ConfirmEmail,
    DefaultLanguage,
    DefaultTheme,
    DeleteAccount,
    PasswordChange,
    PasswordForgot,
    PasswordReset,
    Settings,
    Signin,
    Signup
} from "./pages";
import {AuthProvider, CsrfProvider, LanguageProvider, ThemeProvider} from "./context";

function App() {
    return (
        <Router>
            <LanguageProvider>
                <ThemeProvider>
                    <AuthProvider>
                        <CsrfProvider>
                            <div className="flex flex-col min-h-dvh">
                                <Navbar/>
                                <Routes>
                                    <Route path="/" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <Cashflash/>
                                        </div>
                                    }/>
                                    <Route path="/sign-in" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <Signin/>
                                        </div>
                                    }/>
                                    <Route path="/sign-up" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <Signup/>
                                        </div>
                                    }/>
                                    <Route path="/confirm-email" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <ConfirmEmail/>
                                        </div>
                                    }/>
                                    <Route path="/settings" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <Settings/>
                                        </div>
                                    }/>
                                    <Route path="/default-theme" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <DefaultTheme/>
                                        </div>
                                    }/>
                                    <Route path="/default-language" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <DefaultLanguage/>
                                        </div>
                                    }/>
                                    <Route path="/password-change" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <PasswordChange/>
                                        </div>
                                    }/>
                                    <Route path="/delete-account" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <DeleteAccount/>
                                        </div>
                                    }/>
                                    <Route path="/password-forgot" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <PasswordForgot/>
                                        </div>
                                    }/>
                                    <Route path="/password-reset" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <PasswordReset/>
                                        </div>
                                    }/>
                                    <Route path="/investment-calculator" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <InvestmentCalculatorForm/>
                                        </div>
                                    }/>
                                    <Route path="/investment-calculator-results" element={
                                        <div className="flex-grow flex items-center justify-center">
                                            <InvestmentCalculatorResults/>
                                        </div>
                                    }/>
                                </Routes>
                            </div>
                        </CsrfProvider>
                    </AuthProvider>
                </ThemeProvider>
            </LanguageProvider>
        </Router>
    )
}

export default App;
