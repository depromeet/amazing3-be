import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import DefaultProfileList from "@/components/page/default-profile/default-profile-list";

const pageHeader: IPageHeader = {
  title: "기본 프로필 목록",
};

const DefaultProfileListPage: IDefaultLayoutPage = () => {
  return (
    <>
      <DefaultProfileList />
    </>
  );
};

DefaultProfileListPage.getLayout = getDefaultLayout;
DefaultProfileListPage.pageHeader = pageHeader;

export default DefaultProfileListPage;
