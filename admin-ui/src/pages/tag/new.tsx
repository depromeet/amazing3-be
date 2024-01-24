import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import TagForm from "@/components/page/tag/tag-form";

const pageHeader: IPageHeader = {
  title: "태그 등록",
};

const TagNewPage: IDefaultLayoutPage = () => {
  return <TagForm  />;
};

TagNewPage.getLayout = getDefaultLayout;
TagNewPage.pageHeader = pageHeader;

export default TagNewPage;
