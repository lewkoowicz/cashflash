import React, {useState} from 'react';
import {InvestmentResponse, createInvestment} from "../../services/investmentCalculator";
import {FormInput, Button} from "./index.ts";
import {useNavigate} from 'react-router-dom';
import {useLanguage} from "../../context";
import {translations} from "../../translations/translations.ts";

const InvestmentCalculatorForm: React.FC = () => {
    const navigate = useNavigate();
    const {language} = useLanguage();
    const [investmentAmount, setInvestmentAmount] = useState('');
    const [investmentLength, setInvestmentLength] = useState('');
    const [reinvest, setReinvest] = useState(true);
    const [results, setResults] = useState<InvestmentResponse | null>(null);
    const [error, setError] = useState<string | null>(null);
    const t = translations[language];

    const handleSubmit = async () => {
        try {
            const response = await createInvestment({investmentAmount, investmentLength, reinvest}, language);
            setResults(response);
            setError(null);
            navigate('/investment-calculator-results', {state: {results: response}});
        } catch (error) {
            if (error instanceof Error) {
                setError('Error calculating bond investment');
                setResults(null);
            }
        }
    };

    return (
        <div className="flex flex-col items-center w-full h-full">
            <h1 className="text-3xl font-bold mb-8 text-center w-full">{t.investmentCalculator.investmentCalculator}</h1>
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput
                    label={t.investmentCalculator.investmentAmount}
                    type="number"
                    value={investmentAmount}
                    onChange={(e) => setInvestmentAmount(e.target.value)}
                />
                <FormInput
                    label={t.investmentCalculator.investmentLength}
                    type="number"
                    value={investmentLength}
                    onChange={(e) => setInvestmentLength(e.target.value)}
                />
                <div className="form-control mb-5 text-left">
                    <label className="label">
                        <span className="label-text font-semibold mb-2">{t.investmentCalculator.reinvest}</span>
                    </label>
                    <label className="swap">
                        <input type="checkbox" checked={reinvest} onChange={(e) => setReinvest(e.target.checked)}/>
                        <div className="swap-on">{t.investmentCalculator.yes}</div>
                        <div className="swap-off">{t.investmentCalculator.no}</div>
                    </label>
                </div>
                <Button onClick={handleSubmit} text={t.investmentCalculator.calculate}/>
                {error && <p className="text-error">{error}</p>}
                {results && (
                    <div className="mt-4 p-4 bg-base-200 rounded-lg shadow w-full">
                        <h2 className="text-2xl font-bold mb-3">{t.investmentCalculator.results}</h2>
                        <div className="stats shadow">
                            <div className="stat">
                                <div className="stat-title">{t.investmentCalculator.totalSavings}</div>
                                <div className="stat-value">{results.totalSavings}</div>
                            </div>
                            <div className="stat">
                                <div className="stat-title">{t.investmentCalculator.savingsWithoutInvestment}</div>
                                <div className="stat-value">{results.totalSavingsWithoutInvestment}</div>
                            </div>
                            <div className="stat">
                                <div className="stat-title">{t.investmentCalculator.difference}</div>
                                <div className="stat-value">{results.difference}</div>
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

export default InvestmentCalculatorForm;
