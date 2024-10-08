import {useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {Alert, Button, FormInput} from "../components/ui";
import {useState} from "react";
import {forgotPasword} from "../services";
import {useAlert} from "../hooks";

const PasswordForgot = () => {
    const {language} = useLanguage();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();
    const [email, setEmail] = useState('');

    const t = translations[language];

    const handleSubmit = async () => {
        try {
            const data = await forgotPasword(email, language);
            showAlert('success', data.statusMsg);
        } catch (error) {
            if (error instanceof Error) {
                showAlert('error', error.message);
            }
        }
    }

    return (
        <div className="relative flex flex-col items-center justify-center w-full h-full text-base-content gap-4">
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.forgotPassword.enterEmail}</h1>
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput label={t.forgotPassword.email}
                           type="string"
                           value={email}
                           onChange={e => setEmail(e.target.value)}/>
                <Button className="btn-primary mt-4"
                        onClick={handleSubmit}
                        text={t.forgotPassword.send}/>
            </div>
        </div>
    )
}

export default PasswordForgot;
