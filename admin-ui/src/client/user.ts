import useSWR from "swr";

export interface IUser {
    id: number;
    email: string;
    username: string;
    nickname: string;
    birth: string;
    image: string;
    provider: string;
    authority: string;
}
export interface IUsersResponse {
    result: number;
    body: IUser[]
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export interface IUserResponse {
    result: number;
    body: IUser
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export const useUsers = () => {
    return useSWR<IUsersResponse>(`user`);
};

export const useUser = (id: string | number) => {
    return useSWR<IUserResponse>(`user/${id}`);
};
