import useSWR from "swr";
import {fetchApi, uploadApi} from "@/client/base";

export interface ISticker {
    id: number;
    name: string;
    url: string;
}

export interface IStickerFormValue extends Omit<ISticker, "id" | "url"> {
    image: {
        file: File
        fileList: File[]
    }
}

export interface IStickersResponse {
    result: number;
    body: ISticker[]
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export interface IStickerResponse {
    result: number;
    body: ISticker
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export const useStickers = () => {
    return useSWR<IStickersResponse>(`sticker`);
};

export const useSticker = (id: string | number) => {
    return useSWR<IStickerResponse>(`sticker/${id}`);
};

export const createSticker = (value: IStickerFormValue) => {
    const form = new FormData()
    form.append("name", value.name)
    form.append("image", value.image.file, value.image.file.name)

    return uploadApi.post(`sticker`, { body: form });
};

export const updateSticker = (id: string, value: IStickerFormValue) => {
    const form = new FormData()
    form.append("name", value.name)
    form.append("image", value.image.file, value.image.file.name)

    return uploadApi.patch(`sticker/${id}`, { body: form });
};

export const deleteSticker = (id: string | number) => {
    return fetchApi.delete(`sticker/${id}`);
};
