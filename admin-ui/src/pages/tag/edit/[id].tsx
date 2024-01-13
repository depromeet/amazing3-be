import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import { Alert, Skeleton } from "antd";
import { useRouter } from "next/router";
import {useTag} from "@/client/tag";
import TagForm from "@/components/page/tag/tag-form";

const pageHeader: IPageHeader = {
  title: "태그 수정",
};

const ProductEditPage: IDefaultLayoutPage = () => {
  const router = useRouter();
  const { data, error, isLoading, isValidating } = useTag(router.query.id as string);

  if (error) {
    return <Alert message="데이터 로딩 중 오류가 발생했습니다." type="warning" className="my-5" />;
  }

  if (!data || isLoading || isValidating) {
    return <Skeleton className="my-5" />;
  }

  return <TagForm id={router.query.id as string} initialValues={data.body} />;
};

ProductEditPage.getLayout = getDefaultLayout;
ProductEditPage.pageHeader = pageHeader;

export default ProductEditPage;
