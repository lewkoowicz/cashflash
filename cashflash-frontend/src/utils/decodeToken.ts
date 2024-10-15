import {jwtDecode} from 'jwt-decode';

export const decodeToken = (token: string) => {
    try {
        const decoded: any = jwtDecode(token);
        const userId = decoded.userId;
        const email = decoded.sub;
        const provider = decoded.provider;
        const exp = decoded.exp;
        const role = decoded.scope;
        return {userId, email, provider, role, exp};
    } catch (error) {
        console.error(error);
        return null;
    }
};
