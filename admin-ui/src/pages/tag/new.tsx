import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import TagForm from "@/components/page/tag/tag-form";

const pageHeader: IPageHeader = {
  title: "태그 등록",
};

const ProductNewPage: IDefaultLayoutPage = () => {
  return <TagForm  />;
};

ProductNewPage.getLayout = getDefaultLayout;
ProductNewPage.pageHeader = pageHeader;

export default ProductNewPage;
