import {Link} from "react-router-dom";

const Cashflash = () => {
    return (
        <div>
            <span>
                <Link to="/investment-calculator" className="btn text-blue-500 hover:text-blue-700 mr-4">Investment Calculator</Link>
                <Link to="/budget-tracker" className="btn text-blue-500 hover:text-blue-700 mr-4">Budget Tracker</Link>
                <Link to="/investment-tracker" className="btn text-blue-500 hover:text-blue-700">Investment Tracker</Link>
            </span>
        </div>
    )
}

export default Cashflash;
