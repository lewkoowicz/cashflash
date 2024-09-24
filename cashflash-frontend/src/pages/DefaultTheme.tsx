import {useAuth, useLanguage, useTheme} from "../context";
import {translations} from "../translations/translations.ts";
import {Button} from "../components/ui";
import {setDefaultTheme} from "../services";
import {decodeToken} from "../utils";

const DefaultTheme = () => {
    const {language} = useLanguage();
    const {toggleTheme, theme} = useTheme();
    const {token} = useAuth();

    const decoded = decodeToken(token);

    const t = translations[language];

    const handleSaveButton = async () => {
        try {
            if (decoded) {
                const userId = decoded.userId;
                const email = decoded.email;
                await setDefaultTheme(userId, theme, email, language, token);
            }
        } catch (error) {
            if (error instanceof Error) {
                console.error(error);
            }
        }
    }

    return (
        <div className="flex flex-col w-full max-w-sm items-center justify-center p-4">
            <div className="form-control w-full max-w-sm p-8 bg-base-200 shadow-xl rounded-lg">
                <h2 className="text-lg font-bold mb-6">{t.defaultTheme.setDefaultTheme}</h2>
                <div className="form-control">
                    <label className="cursor-pointer flex items-center">
                        <input type="radio" name="radio-10" onChange={toggleTheme} className="radio checked:bg-blue-900"
                               defaultChecked/>
                        <span className="ml-2">{t.defaultTheme.dark}</span>
                    </label>
                </div>
                <div className="form-control mt-4">
                    <label className="cursor-pointer flex items-center">
                        <input type="radio" name="radio-10" onChange={toggleTheme}
                               className="radio checked:bg-blue-500"/>
                        <span className="ml-2">{t.defaultTheme.light}</span>
                    </label>
                </div>
                <Button className={"btn-primary mt-6 flex items-center justify-center"}
                        onClick={handleSaveButton}
                        text={t.defaultTheme.save}/>
            </div>
        </div>
    )
}

export default DefaultTheme;
