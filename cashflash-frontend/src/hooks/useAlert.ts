import {useState, useCallback} from 'react';

type AlertType = 'success' | 'error' | '';

export const useAlert = () => {
    const [alertType, setAlertType] = useState<AlertType>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [alertKey, setAlertKey] = useState(0);

    const showAlert = useCallback((type: AlertType, message: string) => {
        setAlertType(type);
        setAlertMessage(message);
        setAlertKey(prevKey => prevKey + 1);
    }, []);

    const clearAlert = useCallback(() => {
        setAlertType('');
        setAlertMessage('');
    }, []);

    return {alertType, alertMessage, alertKey, showAlert, clearAlert};
};
