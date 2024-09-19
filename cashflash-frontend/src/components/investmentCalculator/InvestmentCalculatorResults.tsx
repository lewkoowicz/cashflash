import {useLocation, useNavigate} from 'react-router-dom';
import {InvestmentResponse} from "../../services/investmentCalculator";
import {useLanguage} from "../../context";
import {translations} from "../../translations/translations.ts";

const InvestmentCalculatorResults = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const {language} = useLanguage();
    const {results} = location.state as { results: InvestmentResponse };

    const t = translations[language];

    const handleGoBack = () => {
        navigate("/investment-calculator");
    };

    return (
        <div className="flex items-center justify-center h-full p-4 md:p-10">
            <div className="w-full max-w-xs sm:max-w-md md:max-w-lg lg:max-w-4xl bg-base-200 rounded-lg shadow">
                <h1 className="text-xl sm:text-2xl md:text-3xl font-bold mb-4 mt-4 text-center">{t.investmentCalculator.results}</h1>
                <button onClick={handleGoBack} className="btn btn-primary mb-4 mt-4 ml-6">{t.investmentCalculator.goBack}</button>
                {results && (
                    <div className="p-2 sm:p-4">
                        <div className="flex flex-col md:flex-row items-center justify-center stats shadow">
                            <div className="stat">
                                <div className="stat-title text-lg">{t.investmentCalculator.totalSavings}</div>
                                <div className="stat-value text-lg">{results.totalSavings}</div>
                            </div>
                            <div className="stat">
                                <div className="stat-title text-lg">{t.investmentCalculator.savingsWithoutInvestment}</div>
                                <div className="stat-value text-lg">{results.totalSavingsWithoutInvestment}</div>
                            </div>
                            <div className="stat">
                                <div className="stat-title text-lg">{t.investmentCalculator.difference}</div>
                                <div className="stat-value text-lg">{results.difference}</div>
                            </div>
                        </div>
                        <div className="overflow-x-auto mt-4">
                            <table className="table w-full">
                                <thead>
                                <tr>
                                    <th>{t.investmentCalculator.details}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {results.investmentDetails.map((detail, index) => (
                                    <tr key={index}>
                                        <td>{detail}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default InvestmentCalculatorResults;
