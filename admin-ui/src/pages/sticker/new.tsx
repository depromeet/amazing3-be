import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import StickerForm from "@/components/page/sticker/sticker-form";

const pageHeader: IPageHeader = {
  title: "스티커 등록",
};

const ProductNewPage: IDefaultLayoutPage = () => {
  return <StickerForm  />;
};

ProductNewPage.getLayout = getDefaultLayout;
ProductNewPage.pageHeader = pageHeader;

export default ProductNewPage;
