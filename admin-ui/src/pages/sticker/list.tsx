import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import StickerList from "@/components/page/sticker/sticker-list";

const pageHeader: IPageHeader = {
  title: "스티커 목록",
};

const ProductListPage: IDefaultLayoutPage = () => {
  return (
    <>
      <StickerList />
    </>
  );
};

ProductListPage.getLayout = getDefaultLayout;
ProductListPage.pageHeader = pageHeader;

export default ProductListPage;
