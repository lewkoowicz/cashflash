import {useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {useEffect, useState} from "react";
import {Alert, Button, FormInput} from "../components/ui";
import {resetPassword} from "../services";
import {useAlert} from "../hooks";
import {useNavigate} from "react-router-dom";

const PasswordReset = () => {
    const {language} = useLanguage();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();
    const navigate = useNavigate();
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');
    const [tokenFromParams, setTokenFromParams] = useState('');

    const t = translations[language];

    useEffect(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const token = searchParams.get('resetToken');
        setTokenFromParams(token || '');
    }, []);

    const handleSubmit = async () => {
        if (newPassword !== confirmNewPassword) {
            showAlert('error', t.signup.passwordsDoNotMatch)
            return;
        }
        try {
            const data = await resetPassword(tokenFromParams, newPassword, language);
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
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.resetPassword.resetPassword}</h1>
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput label={t.resetPassword.newPassword}
                           type="password"
                           value={newPassword}
                           onChange={e => setNewPassword(e.target.value)}/>
                <FormInput label={t.resetPassword.confirmPassword}
                           type="password"
                           value={confirmNewPassword}
                           onChange={e => setConfirmNewPassword(e.target.value)}/>
                <Button className="btn-primary mt-4"
                        onClick={handleSubmit}
                        text={t.resetPassword.resetPassword}/>
            </div>
        </div>
    )
}

export default PasswordReset;
