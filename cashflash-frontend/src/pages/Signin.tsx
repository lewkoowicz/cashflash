import {useState} from 'react';
import {useAuth, useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {Alert, Button, FormInput} from "../components/ui";
import {Link, useNavigate} from "react-router-dom";
import {BASE_URL} from "../services";
import {useAlert} from "../hooks";

const Signin = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const {alertType, alertMessage, alertKey, showAlert} = useAlert();
    const {language} = useLanguage();
    const {signin} = useAuth();
    const navigate = useNavigate();

    const t = translations[language];

    const handleSubmit = async () => {
        try {
            await signin(email, password);
            navigate('/');
        } catch (error) {
            if (error instanceof Error) {
                showAlert('error', error.message);
            }
        }
    };

    const handleGoogleSignIn = () => {
        window.location.href = `${BASE_URL}/oauth2/authorization/google`;
    };

    return (
        <div className="relative flex flex-col items-center justify-center w-full h-full text-base-content gap-4">
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.signin.signin}</h1>
            {alertType && (
                <Alert key={alertKey} type={alertType} message={alertMessage}/>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput label={t.signin.email}
                           type="email" placeholder=""
                           value={email}
                           onChange={e => setEmail(e.target.value)}/>
                <FormInput label={t.signin.password}
                           type="password"
                           placeholder=""
                           value={password}
                           onChange={e => setPassword(e.target.value)}/>
                <Link to="/password-forgot"
                      className="mt-2 text-blue-500 hover:text-blue-700">{t.signin.forgotPassword}</Link>
                <Button className={"btn-primary mt-4"}
                        onClick={handleSubmit}
                        text={t.signin.signin}/>
                <Button className={"btn-outline mt-4 flex items-center justify-center"}
                        onClick={handleGoogleSignIn}
                        text="">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 488 512"
                         className="w-6 h-6 mr-2">
                        <path
                            d="M488 261.8C488 403.3 391.1 504 248 504 110.8 504 0 393.2 0 256S110.8 8 248 8c66.8 0 123 24.5 166.3 64.9l-67.5 64.9C258.5 52.6 94.3 116.6 94.3 256c0 86.5 69.1 156.6 153.7 156.6 98.2 0 135-70.4 140.8-106.9H248v-85.3h236.1c2.3 12.7 3.9 24.9 3.9 41.4z"/>
                    </svg>
                    {t.signin.signInWithGoogle}
                </Button>
                <div className="mt-6 text-center">
                    <p className="mt-2">{t.signin.noAccount}</p>
                    <Link to="/sign-up" className="text-blue-500 hover:text-blue-700">{t.signin.signup}</Link>
                </div>
            </div>
        </div>
    );
};

export default Signin;
