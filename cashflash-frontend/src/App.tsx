import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {InvestmentCalculatorForm, InvestmentCalculatorResults} from "./components/investmentCalculator";
import {Navbar} from "./layouts";
import {Cashflash} from "./pages";

function App() {
    return (
        <Router>
            <div className="flex flex-col min-h-screen">
                <Navbar/>
                <Routes>
                    <Route path="/" element={
                        <div className="flex-grow flex items-center justify-center">
                            <Cashflash/>
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
        </Router>
    )
}

export default App;
