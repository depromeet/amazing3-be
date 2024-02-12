import useSWR from "swr";

export interface ILifeMap {
    id: number;
    isPublic: boolean;
    goals: IGoal[];
    goalCount: number;
    viewCount: number;
    cheeringCount: number;
}

export interface IGoal {
    title: string;
    description: string;
    deadline: string;
    stickerUrl: string
    tagInfo: ITag;
    tasks: ITask[]
}

export interface ITag {
    tagId: number;
    tagContent: string;
}


export interface ITask {
    taskId: number;
    isTaskDone: boolean;
    taskDescription: string;
}
export interface ILifeMapResponse {
    result: number;
    body: ILifeMap
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export const useLifeMap = (id: string | number) => {
    return useSWR<ILifeMapResponse>(`life-map?userId=${id}`);
};
