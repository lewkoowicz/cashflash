import {useAuth, useLanguage, useTheme} from "../context";
import {translations} from "../translations/translations.ts";
import {Alert, Button} from "../components/ui";
import {setDefaultTheme} from "../services";
import {decodeToken} from "../utils";
import {useAlert} from "../hooks";

const DefaultTheme = () => {
    const {language} = useLanguage();
    const {toggleTheme, theme} = useTheme();
    const {token} = useAuth();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();

    const decoded = decodeToken(token);

    const t = translations[language];

    const handleSaveButton = async () => {
        try {
            if (decoded) {
                const userId = decoded.userId;
                const email = decoded.email;
                const data = await setDefaultTheme(userId, theme, email, language, token);
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
                    <h2 className="text-lg font-bold mb-6">{t.defaultTheme.setDefaultTheme}</h2>
                    <div className="form-control">
                        <label className="cursor-pointer flex items-center">
                            <input type="radio" name="radio-10" onChange={toggleTheme}
                                   className="radio checked:bg-blue-900"
                                   checked={theme === 'dark'}/>
                            <span className="ml-2">{t.defaultTheme.dark}</span>
                            <div className="absolute right-40">
                                <svg className="fill-current w-7 h-7" xmlns="http://www.w3.org/2000/svg"
                                     viewBox="0 0 24 24">
                                    <path
                                        d="M21.64,13a1,1,0,0,0-1.05-.14,8.05,8.05,0,0,1-3.37.73A8.15,8.15,0,0,1,9.08,5.49a8.59,8.59,0,0,1,.25-2A1,1,0,0,0,8,2.36,10.14,10.14,0,1,0,22,14.05,1,1,0,0,0,21.64,13Zm-9.5,6.69A8.14,8.14,0,0,1,7.08,5.22v.27A10.15,10.15,0,0,0,17.22,15.63a9.79,9.79,0,0,0,2.1-.22A8.11,8.11,0,0,1,12.14,19.73Z"/>
                                </svg>
                            </div>
                        </label>
                    </div>
                    <div className="form-control mt-4">
                        <label className="cursor-pointer flex items-center">
                            <input type="radio" name="radio-10" onChange={toggleTheme}
                                   className="radio checked:bg-blue-500"
                                   checked={theme === 'light'}/>
                            <span className="ml-2">{t.defaultTheme.light}</span>
                            <div className="absolute right-40">
                                <svg className="fill-current w-8 h-8" xmlns="http://www.w3.org/2000/svg"
                                     viewBox="0 0 24 24">
                                    <path
                                        d="M5.64,17l-.71.71a1,1,0,0,0,0,1.41,1,1,0,0,0,1.41,0l.71-.71A1,1,0,0,0,5.64,17ZM5,12a1,1,0,0,0-1-1H3a1,1,0,0,0,0,2H4A1,1,0,0,0,5,12Zm7-7a1,1,0,0,0,1-1V3a1,1,0,0,0-2,0V4A1,1,0,0,0,12,5ZM5.64,7.05a1,1,0,0,0,.7.29,1,1,0,0,0,.71-.29,1,1,0,0,0,0-1.41l-.71-.71A1,1,0,0,0,4.93,6.34Zm12,.29a1,1,0,0,0,.7-.29l.71-.71a1,1,0,1,0-1.41-1.41L17,5.64a1,1,0,0,0,0,1.41A1,1,0,0,0,17.66,7.34ZM21,11H20a1,1,0,0,0,0,2h1a1,1,0,0,0,0-2Zm-9,8a1,1,0,0,0-1,1v1a1,1,0,0,0,2,0V20A1,1,0,0,0,12,19ZM18.36,17A1,1,0,0,0,17,18.36l.71.71a1,1,0,0,0,1.41,0,1,1,0,0,0,0-1.41ZM12,6.5A5.5,5.5,0,1,0,17.5,12,5.51,5.51,0,0,0,12,6.5Zm0,9A3.5,3.5,0,1,1,15.5,12,3.5,3.5,0,0,1,12,15.5Z"/>
                                </svg>
                            </div>
                        </label>
                    </div>
                </div>
                <Button className={"btn-primary mt-6 flex items-center justify-center"}
                        onClick={handleSaveButton}
                        text={t.defaultTheme.save}/>
            </div>
        </div>
    )
}

export default DefaultTheme;
