import useSWR from "swr";
import {fetchApi, uploadApi} from "@/client/base";

export interface IDefaultProfile {
    id: number;
    name: string;
    url: string;
}

export interface IDefaultProfileFormValue extends Omit<IDefaultProfile, "id" | "url"> {
    image: {
        file: File
        fileList: File[]
    }
}

export interface IDefaultProfilesResponse {
    result: number;
    body: IDefaultProfile[]
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export interface IDefaultProfileResponse {
    result: number;
    body: IDefaultProfile
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export const useDefaultProfiles = () => {
    return useSWR<IDefaultProfilesResponse>(`profile/default`);
};

export const useDefaultProfile = (id: string | number) => {
    return useSWR<IDefaultProfileResponse>(`profile/default/${id}`);
};

export const createDefaultProfile = (value: IDefaultProfileFormValue) => {
    const form = new FormData()
    form.append("name", value.name)
    form.append("image", value.image.file, value.image.file.name)

    return uploadApi.post(`profile/default`, { body: form });
};

export const updateDefaultProfile = (id: string, value: IDefaultProfileFormValue) => {
    const form = new FormData()
    form.append("name", value.name)
    form.append("image", value.image.file, value.image.file.name)

    return uploadApi.patch(`profile/default/${id}`, { body: form });
};

export const deleteDefaultProfile = (id: string | number) => {
    return fetchApi.delete(`profile/default/${id}`);
};
