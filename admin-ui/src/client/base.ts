import ky from "ky-universal";

export const fetcher = (input: URL | RequestInfo, init?: RequestInit | undefined) =>
  ky(`${process.env.NEXT_PUBLIC_API_ENDPOINT}/${input}`, init).then((res) => res.json());

export const fetchApi = ky.create({
  prefixUrl: process.env.NEXT_PUBLIC_API_ENDPOINT,
  headers: {
    "Content-Type": "application/json",
  },
});

export const uploadApi = ky.create({
  prefixUrl: process.env.NEXT_PUBLIC_API_ENDPOINT,
  // Content-Type: multipart/form-data를 헤더에 명시하면 오류
  // 브라우저에서 자동으로 파일 타입을 지정해줌 (multipart/form-data; boundary-----)
});