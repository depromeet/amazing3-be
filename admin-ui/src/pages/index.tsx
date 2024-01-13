import { useDashboard } from "@/client/dashboard";
import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import MainCalendar from "@/components/page/index/main-calendar";
import DashboardStatistics from "@/components/page/index/dashboard-statistics";
import { useAuth } from "@/lib/auth/auth-provider";
import { Alert, Divider, Skeleton } from "antd";

const pageHeader: IPageHeader = {
  title: "Welcome",
};

const IndexPage: IDefaultLayoutPage = () => {
  const { session } = useAuth();
  const {data, error}  = useDashboard();

  return (
    <>
      <h2 className="title">👋 {session.user.name || "관리자"}님 안녕하세요!</h2>

      <div className="my-5">
        {data ? (
          <DashboardStatistics data={data} />
        ) : error ? (
          <Alert message="대시보드 API 호출 중 오류가 발생했습니다." type="warning" />
        ) : (
          <Skeleton />
        )}
      </div>

      <Divider />

      <h3 className="title">달력</h3>

      <MainCalendar />
    </>
  );
};

IndexPage.getLayout = getDefaultLayout;
IndexPage.pageHeader = pageHeader;

export default IndexPage;
