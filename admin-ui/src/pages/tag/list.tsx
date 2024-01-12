import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import TagList from "@/components/page/tag/tag-list";

const pageHeader: IPageHeader = {
  title: "태그 목록",
};

const ProductListPage: IDefaultLayoutPage = () => {
  return (
    <>
      <TagList />
    </>
  );
};

ProductListPage.getLayout = getDefaultLayout;
ProductListPage.pageHeader = pageHeader;

export default ProductListPage;
