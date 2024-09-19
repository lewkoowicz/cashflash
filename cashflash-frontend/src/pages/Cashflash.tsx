import {Link} from "react-router-dom";
import {translations} from "../translations/translations.ts";
import {useLanguage} from "../context";

const Cashflash = () => {
    const {language} = useLanguage();
    const t = translations[language];

    return (
        <div>
            <span>
                <Link to="/investment-calculator" className="btn text-blue-500 hover:text-blue-700 mr-4">{t.cashflash.investmentCalculator}</Link>
                <Link to="/budget-tracker" className="btn text-blue-500 hover:text-blue-700 mr-4">{t.cashflash.budgetTracker}</Link>
                <Link to="/investment-tracker" className="btn text-blue-500 hover:text-blue-700">{t.cashflash.investmentTracker}</Link>
            </span>
        </div>
    )
}

export default Cashflash;
