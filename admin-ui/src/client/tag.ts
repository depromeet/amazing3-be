import useSWR from "swr";
import {fetchApi} from "@/client/base";

export interface ITag {
    id: number;
    content: string;
}

export interface ITagFormValue extends ITag {}
export interface ITagsResponse {
    result: number;
    body: ITag[]
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export interface ITagResponse {
    result: number;
    body: ITag
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export const useTags = () => {
    return useSWR<ITagsResponse>(`tag`);
};

export const useTag = (id: string | number) => {
    return useSWR<ITagResponse>(`tag/${id}`);
};

export const createTag = (value: ITagFormValue) => {
    return fetchApi.post(`tag`, { body: JSON.stringify(value) });
};

export const updateTag = (id: string | number, value: ITagFormValue) => {
    return fetchApi.patch(`tag/${id}`, { body: JSON.stringify(value) });
};

export const deleteTag = (id: string | number) => {
    return fetchApi.delete(`tag/${id}`);
};
