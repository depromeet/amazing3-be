import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import StickerList from "@/components/page/sticker/sticker-list";

const pageHeader: IPageHeader = {
  title: "스티커 목록",
};

const StickerListPage: IDefaultLayoutPage = () => {
  return (
    <>
      <StickerList />
    </>
  );
};

StickerListPage.getLayout = getDefaultLayout;
StickerListPage.pageHeader = pageHeader;

export default StickerListPage;
