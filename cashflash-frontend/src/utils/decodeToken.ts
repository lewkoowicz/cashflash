import {jwtDecode} from 'jwt-decode';

export const decodeToken = (token: string) => {
    try {
        const decoded: any = jwtDecode(token);
        const userId = decoded.userId;
        const email = decoded.sub;
        return {userId, email};
    } catch (error) {
        console.error(error);
        return null;
    }
};
