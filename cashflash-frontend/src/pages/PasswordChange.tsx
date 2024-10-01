import {useAuth, useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {Alert, Button, FormInput} from "../components/ui";
import {useState} from "react";
import {useAlert} from "../hooks";
import {decodeToken} from "../utils";
import {changePassword} from "../services";

const PasswordChange = () => {
    const {language} = useLanguage();
    const {token} = useAuth();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');

    const decoded = decodeToken(token);

    const t = translations[language];

    const handleSubmit = async () => {
        if (newPassword !== confirmNewPassword) {
            showAlert('error', t.signup.passwordsDoNotMatch)
            return;
        }
        try {
            if (decoded) {
                const email = decoded.email;
                const data = await changePassword({currentPassword, newPassword}, language, token, email);
                showAlert('success', data.statusMsg);
            }
        } catch (error) {
            if (error instanceof Error) {
                showAlert('error', error.message);
            }
        }
    }

    return (
        <div className="relative flex flex-col items-center justify-center w-full h-full text-base-content gap-4">
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.changePassword.changePassword}</h1>
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput label={t.changePassword.currentPassword}
                           type="password"
                           value={currentPassword}
                           onChange={e => setCurrentPassword(e.target.value)}/>
                <FormInput label={t.changePassword.newPassword}
                           type="password"
                           value={newPassword}
                           onChange={e => setNewPassword(e.target.value)}/>
                <FormInput label={t.changePassword.confirmNewPassword}
                           type="password"
                           value={confirmNewPassword}
                           onChange={e => setConfirmNewPassword(e.target.value)}/>
                <Button className="btn-primary mt-4"
                        onClick={handleSubmit}
                        text={t.changePassword.submit}/>
            </div>
        </div>
    )
}

export default PasswordChange;
