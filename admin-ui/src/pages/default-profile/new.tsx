import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import DefaultProfileForm from "@/components/page/default-profile/default-profile-form";

const pageHeader: IPageHeader = {
  title: "기본 프로필 등록",
};

const DefaultProfileNewPage: IDefaultLayoutPage = () => {
  return <DefaultProfileForm  />;
};

DefaultProfileNewPage.getLayout = getDefaultLayout;
DefaultProfileNewPage.pageHeader = pageHeader;

export default DefaultProfileNewPage;
