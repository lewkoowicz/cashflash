import {CsrfContext} from "../index";
import {useContext} from "react";

export const useCsrf = () => {
    const context = useContext(CsrfContext);
    if (context === undefined) {
        throw new Error('useCsrf must be used within an CsrfProvider');
    }
    return context;
}
