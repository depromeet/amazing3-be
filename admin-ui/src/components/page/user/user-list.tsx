import DefaultTable from "@/components/shared/ui/default-table";
import {Alert, Button, Modal} from "antd";
import { ColumnsType } from "antd/es/table";
import { useRouter } from "next/router";
import React, {useCallback} from "react";
import {IUser, useUsers} from "@/client/user";

const UserList = () => {
  const router = useRouter();

  const { data, error, isLoading } = useUsers();

  const handleChangePage = useCallback(
    (pageNumber: number) => {
      router.push({
        pathname: router.pathname,
        query: { ...router.query, page: pageNumber },
      });
    },
    [router]
  );

  const convertProvider = (provider: string) => {
    if(provider === 'KAKAO') {
      return '카카오'
    } else if (provider === 'GOOGLE') {
      return '구글'
    } else if (provider === 'NAVER') {
      return '네이버'
    }

    return '알 수 없는 경로'
  }

    const openLifeMapPopup = (userId: number) => {
        window.open(`/popup/life-map/${userId}`, 'LifeMap', 'width=1000, height=610');
    };



  const columns: ColumnsType<IUser> = [
    {
      title: "인생 지도",
      key: "action",
      width: 120,
      align: "center",
      render: (_value: unknown, record: IUser) => {
        return (
          <span className="flex justify-center gap-2">
            <Button type="primary" onClick={() => openLifeMapPopup(record.id)}>
              보기
            </Button>
          </span>
        );
      },
    },
    {
      title: "유저 ID",
      dataIndex: "id",
      width: 100,
      render: (value: string, record: IUser) => {
        return (
            <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.id}</span>
          </span>
        );
      },
    },
    {
      title: "유저 이름 (username)",
      dataIndex: "username",
      render: (value: string, record: IUser) => {
        return (
          <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.username}</span>
          </span>
        );
      },
    },
    {
      title: "유저 별명 (nickname)",
      dataIndex: "nickname",
      render: (value: string, record: IUser) => {
        return (
          <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.nickname}</span>
          </span>
        );
      },
    },
    {
      title: "생년월",
      dataIndex: "birth",
      render: (value: string, record: IUser) => {
        return (
          <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.birth}</span>
          </span>
        );
      },
    },
    {
      title: "유저 이미지",
      dataIndex: "image",
      render: (value: string, record: IUser) => {
        return (
          <span>
            <img src={record.image} height={100} width={100} className="px-2 py-1 mr-1 bg-gray-100 rounded" />
          </span>
        );
      },
    },
    {
      title: "가입 경로",
      dataIndex: "provider",
      render: (value: string, record: IUser) => {
        return (
          <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{convertProvider(record.provider)}</span>
          </span>
          );
      },
    },
  ];

  if (error) {
    return <Alert message="데이터 로딩 중 오류가 발생했습니다." type="warning" />;
  }

  return (
    <>
      <DefaultTable<IUser>
        columns={columns}
        dataSource={data?.body || []}
        loading={isLoading}
        pagination={{
          current: Number(router.query.page || 1),
          defaultPageSize: 10,
          total: data?.body.length || 0,
          showSizeChanger: false,
          onChange: handleChangePage,
        }}
        className="mt-3"
        countLabel={data?.body.length}
      />
    </>
  );
};

export default React.memo(UserList);
