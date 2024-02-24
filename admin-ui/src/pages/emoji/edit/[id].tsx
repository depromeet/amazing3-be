import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import { Alert, Skeleton } from "antd";
import { useRouter } from "next/router";
import EmojiForm from "@/components/page/emoji/emoji-form";
import {useEmoji} from "@/client/emoji";

const pageHeader: IPageHeader = {
  title: "이모지 수정",
};

const EmojiEditPage: IDefaultLayoutPage = () => {
  const router = useRouter();
  const { data, error, isLoading, isValidating } = useEmoji(router.query.id as string);

  if (error) {
    return <Alert message="데이터 로딩 중 오류가 발생했습니다." type="warning" className="my-5" />;
  }

  if (!data || isLoading || isValidating) {
    return <Skeleton className="my-5" />;
  }

  return <EmojiForm id={router.query.id as string} initialValues={data.body} />;
};

EmojiEditPage.getLayout = getDefaultLayout;
EmojiEditPage.pageHeader = pageHeader;

export default EmojiEditPage;
