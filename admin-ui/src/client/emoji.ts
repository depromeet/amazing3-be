import useSWR from "swr";
import {fetchApi, uploadApi} from "@/client/base";

const emoji = `emoji`

export interface IEmoji {
    id: number;
    name: string;
    url: string;
}

export interface IEmojiFormValue extends Omit<IEmoji, "id" | "url"> {
    image: {
        file: File
        fileList: File[]
    }
}

export interface IEmojisResponse {
    result: number;
    body: IEmoji[]
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export interface IEmojiResponse {
    result: number;
    body: IEmoji
    error: {
        code: string;
        message: string;
        data: object;
    };
}

export const useEmojis = () => {
    return useSWR<IEmojisResponse>(emoji);
};

export const useEmoji = (id: string | number) => {
    return useSWR<IEmojiResponse>(`${emoji}/${id}`);
};

export const createEmoji = (value: IEmojiFormValue) => {
    const form = new FormData()
    form.append("name", value.name)
    form.append("image", value.image.file, value.image.file.name)

    return uploadApi.post(emoji, { body: form });
};

export const updateEmoji = (id: string, value: IEmojiFormValue) => {
    const form = new FormData()
    form.append("name", value.name)
    form.append("image", value.image.file, value.image.file.name)

    return uploadApi.patch(`${emoji}/${id}`, { body: form });
};

export const deleteEmoji = (id: string | number) => {
    return fetchApi.delete(`${emoji}/${id}`);
};
