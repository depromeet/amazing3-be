import useSWR from "swr";

export interface IGoal {
    id: number;
    email: string;
    username: string;
    nickname: string;
    birth: string;
    image: string;
    provider: string;
    authority: string;
}

export interface IGoalFormValue extends IGoal {}
export interface IGoalsResponse {
    result: number;
    body: IGoal[]
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export interface IGoalResponse {
    result: number;
    body: IGoal
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export const useGoals = (userId: number) => {
    return useSWR<IGoalsResponse>(`goal?userId=${userId}`);
};

export const useGoal = (id: string | number) => {
    return useSWR<IGoalResponse>(`user/${id}/goal`);
};
