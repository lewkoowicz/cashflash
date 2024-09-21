import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {InvestmentCalculatorForm, InvestmentCalculatorResults} from "./components/investmentCalculator";
import {Navbar} from "./layouts";
import {Cashflash, Signin, Signup} from "./pages";
import {AuthProvider, CsrfProvider, LanguageProvider, ThemeProvider} from "./context";

function App() {
    return (
        <Router>
            <LanguageProvider>
                <ThemeProvider>
                    <AuthProvider>
                        <CsrfProvider>
                            <div className="flex flex-col min-h-screen">
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
