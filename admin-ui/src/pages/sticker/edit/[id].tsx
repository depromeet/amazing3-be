import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import { Alert, Skeleton } from "antd";
import { useRouter } from "next/router";
import StickerForm from "@/components/page/sticker/sticker-form";
import {useSticker} from "@/client/sticker";

const pageHeader: IPageHeader = {
  title: "태그 수정",
};

const StickerEditPage: IDefaultLayoutPage = () => {
  const router = useRouter();
  const { data, error, isLoading, isValidating } = useSticker(router.query.id as string);

  if (error) {
    return <Alert message="데이터 로딩 중 오류가 발생했습니다." type="warning" className="my-5" />;
  }

  if (!data || isLoading || isValidating) {
    return <Skeleton className="my-5" />;
  }

  return <StickerForm id={router.query.id as string} initialValues={data.body} />;
};

StickerEditPage.getLayout = getDefaultLayout;
StickerEditPage.pageHeader = pageHeader;

export default StickerEditPage;
