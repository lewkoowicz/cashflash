import {useAuth, useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {Alert, Button} from "../components/ui";
import {setDefaultLanguage} from "../services";
import {decodeToken} from "../utils";
import {useAlert} from "../hooks";
import polishFlag from "../assets/polishflag.png";
import ukFlag from "../assets/ukflag.png";

const DefaultLanguage = () => {
    const {toggleLanguage, language} = useLanguage();
    const {token} = useAuth();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();

    const decoded = decodeToken(token);

    const t = translations[language];

    const handleSaveButton = async () => {
        try {
            if (decoded) {
                const userId = decoded.userId;
                const email = decoded.email;
                const data = await setDefaultLanguage(userId, email, language, token);
                showAlert('success', data.statusMsg);
            }
        } catch (error) {
            if (error instanceof Error) {
                showAlert('error', error.message);
            }
        }
    }

    return (
        <div className="flex flex-col w-full max-w-sm items-center justify-center p-4">
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-sm p-8 bg-base-200 shadow-xl rounded-lg">
                <div className="relative group">
                    <h2 className="text-lg font-bold mb-6">{t.defaultLanguage.setDefaultLanguage}</h2>
                    <div className="form-control">
                        <label className="cursor-pointer flex items-center">
                            <input type="radio" name="radio-10" onChange={toggleLanguage}
                                   className="radio checked:bg-blue-900"
                                   checked={language === 'pl'}/>
                            <span className="text-sm sm:text-base ml-2">{t.defaultLanguage.polish}</span>
                            <div className="absolute right-40">
                                <img src={polishFlag} alt="Polish flag" className="w-6 h-4"/>
                            </div>
                        </label>
                    </div>
                    <div className="form-control mt-4">
                        <label className="cursor-pointer flex items-center">
                            <input type="radio" name="radio-10" onChange={toggleLanguage}
                                   className="radio checked:bg-blue-500"
                                   checked={language === 'en'}/>
                            <span className="text-sm sm:text-base ml-2">{t.defaultLanguage.english}</span>
                            <div className="absolute right-40">
                                <img src={ukFlag} alt="UK flag" className="w-6 h-4"/>
                            </div>
                        </label>
                    </div>
                </div>
                <Button className={"btn-primary mt-6 flex items-center justify-center"}
                        onClick={handleSaveButton}
                        text={t.defaultLanguage.save}/>
            </div>
        </div>
    )
}

export default DefaultLanguage;
