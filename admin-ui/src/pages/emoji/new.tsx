import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import EmojiForm from "@/components/page/emoji/emoji-form";

const pageHeader: IPageHeader = {
  title: "이모지 등록",
};

const EmojiNewPage: IDefaultLayoutPage = () => {
  return <EmojiForm  />;
};

EmojiNewPage.getLayout = getDefaultLayout;
EmojiNewPage.pageHeader = pageHeader;

export default EmojiNewPage;
