import {jwtDecode} from 'jwt-decode';

export const decodeToken = (token: string) => {
    try {
        const decoded: any = jwtDecode(token);
        const userId = decoded.userId;
        const email = decoded.sub;
        const provider = decoded.provider;
        return {userId, email, provider};
    } catch (error) {
        console.error(error);
        return null;
    }
};
