import React, {useState} from 'react';
import {createInvestment} from "../../services/investmentCalculator";
import {Alert, Button, FormInput} from "../ui";
import {useNavigate} from 'react-router-dom';
import {useLanguage} from "../../context";
import {translations} from "../../translations/translations.ts";
import {useAlert} from "../../hooks";

const InvestmentCalculatorForm: React.FC = () => {
    const navigate = useNavigate();
    const {language} = useLanguage();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();
    const [formData, setFormData] = useState({
        investmentAmount: '',
        investmentLength: '',
        reinvest: true
    });
    const t = translations[language];

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value, type, checked} = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = async () => {
        try {
            const response = await createInvestment(formData, language);
            navigate('/investment-calculator-results', {state: {results: response}});
        } catch (error) {
            if (error instanceof Error) {
                showAlert('error', error.message)
            }
        }
    };

    return (
        <div className="flex flex-col items-center w-full h-full">
            <h1 className="text-3xl font-bold mb-8 text-center w-full">{t.investmentCalculator.investmentCalculator}</h1>
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput
                    label={t.investmentCalculator.investmentAmount}
                    type="number"
                    value={formData.investmentAmount}
                    onChange={handleInputChange}
                />
                <FormInput
                    label={t.investmentCalculator.investmentLength}
                    type="number"
                    value={formData.investmentLength}
                    onChange={handleInputChange}
                />
                <div className="form-control mb-5 text-left">
                    <label className="label">
                        <span className="label-text font-semibold mb-2">{t.investmentCalculator.reinvest}</span>
                    </label>
                    <label className="swap">
                        <input
                            type="checkbox"
                            name="reinvest"
                            checked={formData.reinvest}
                            onChange={handleInputChange}
                        />
                        <div className="swap-on">{t.investmentCalculator.yes}</div>
                        <div className="swap-off">{t.investmentCalculator.no}</div>
                    </label>
                </div>
                <Button className="btn-primary mt-2" onClick={handleSubmit} text={t.investmentCalculator.calculate}/>
            </div>
        </div>
    );
};

export default InvestmentCalculatorForm;
