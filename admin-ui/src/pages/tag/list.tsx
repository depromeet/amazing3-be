import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import TagList from "@/components/page/tag/tag-list";

const pageHeader: IPageHeader = {
  title: "태그 목록",
};

const TagListPage: IDefaultLayoutPage = () => {
  return (
    <>
      <TagList />
    </>
  );
};

TagListPage.getLayout = getDefaultLayout;
TagListPage.pageHeader = pageHeader;

export default TagListPage;
