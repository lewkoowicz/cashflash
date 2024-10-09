import {useEffect, useState} from "react";
import {useLanguage} from "../context";
import {useAlert} from "../hooks";
import {useNavigate} from "react-router-dom";
import {translations} from "../translations/translations.ts";
import {Alert, Button} from "../components/ui";
import {confirmEmail} from "../services";

const ConfirmEmail = () => {
    const {language} = useLanguage();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();
    const navigate = useNavigate();
    const [tokenFromParams, setTokenFromParams] = useState('');

    const t = translations[language];

    useEffect(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const token = searchParams.get('confirmationToken');
        setTokenFromParams(token || '');
    }, []);

    const handleSubmit = async () => {
        try {
            const data = await confirmEmail(tokenFromParams, language);
            showAlert('success', data.statusMsg);
            setTimeout(() => {
                navigate("/sign-in");
            }, 1500)
        } catch (error) {
            if (error instanceof Error) {
                showAlert('error', error.message);
            }
        }
    }

    return (
        <div className="relative flex flex-col items-center justify-center w-full h-full text-base-content gap-4">
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.confirmEmail.confirmEmail}</h1>
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <Button className="btn-primary"
                        onClick={handleSubmit}
                        text={t.confirmEmail.confirm}/>
            </div>
        </div>
    )
}

export default ConfirmEmail;
