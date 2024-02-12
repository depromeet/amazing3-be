import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import UserList from "@/components/page/user/user-list";

const pageHeader: IPageHeader = {
  title: "유저 목록",
};

const UserListPage: IDefaultLayoutPage = () => {
  return (
    <>
      <UserList />
    </>
  );
};

UserListPage.getLayout = getDefaultLayout;
UserListPage.pageHeader = pageHeader;

export default UserListPage;
