import {useAuth, useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {Alert, Button, FormInput} from "../components/ui";
import {useState} from "react";
import {deleteAccount, signout} from "../services";
import {useAlert} from "../hooks";
import {decodeToken} from "../utils";

const DeleteAccount = () => {
    const {language} = useLanguage();
    const {token} = useAuth();
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();
    const [deleteString, setDeleteString] = useState('');

    const decoded = decodeToken(token);

    const t = translations[language];

    const handleSubmit = async () => {
        try {
            if (decoded) {
                const email = decoded.email;
                await deleteAccount(email, deleteString, language, token);
                await signout(language);
                localStorage.removeItem('token');
                const newUrl = `${window.location.origin}/sign-up`;
                window.location.replace(newUrl);
                setTimeout(() => {
                    window.location.reload();
                }, 500);
            }
        } catch (error) {
            if (error instanceof Error) {
                showAlert('error', error.message);
            }
        }
    }

    return (
        <div className="relative flex flex-col items-center justify-center w-full h-full text-base-content gap-4">
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.deleteAccount.deleteAccount}</h1>
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput label={t.deleteAccount.typeInDelete}
                           type="string"
                           value={deleteString}
                           onChange={e => setDeleteString(e.target.value)}/>
                <Button className="btn-error mt-4"
                        onClick={handleSubmit}
                        text={t.deleteAccount.delete}/>
            </div>
        </div>
    )
}

export default DeleteAccount;
