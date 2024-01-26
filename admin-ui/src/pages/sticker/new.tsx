import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import StickerForm from "@/components/page/sticker/sticker-form";

const pageHeader: IPageHeader = {
  title: "스티커 등록",
};

const StickerNewPage: IDefaultLayoutPage = () => {
  return <StickerForm  />;
};

StickerNewPage.getLayout = getDefaultLayout;
StickerNewPage.pageHeader = pageHeader;

export default StickerNewPage;
