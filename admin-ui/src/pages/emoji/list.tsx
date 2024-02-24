import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import EmojiList from "@/components/page/emoji/emoji-list";

const pageHeader: IPageHeader = {
  title: "이모지 목록",
};

const EmojiListPage: IDefaultLayoutPage = () => {
  return (
    <>
      <EmojiList />
    </>
  );
};

EmojiListPage.getLayout = getDefaultLayout;
EmojiListPage.pageHeader = pageHeader;

export default EmojiListPage;
