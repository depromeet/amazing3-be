import DefaultTable from "@/components/shared/ui/default-table";
import DefaultTableBtn from "@/components/shared/ui/default-table-btn";
import {Alert, Button, Dropdown, MenuProps, message, Popconfirm} from "antd";
import { ColumnsType } from "antd/es/table";
import Link from "next/link";
import { useRouter } from "next/router";
import React, { useCallback, useMemo, useState } from "react";
import {deleteSticker, ISticker, useStickers} from "@/client/sticker";

const StickerList = () => {
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [messageApi, contextHolder] = message.useMessage();
  const router = useRouter();

  const { data, error, isLoading } = useStickers();

  const handleChangePage = useCallback(
    (pageNumber: number) => {
      router.push({
        pathname: router.pathname,
        query: { ...router.query, page: pageNumber },
      });
    },
    [router]
  );

  const onSelectChange = useCallback((newSelectedRowKeys: React.Key[]) => {
    setSelectedRowKeys(newSelectedRowKeys);
  }, []);

  const onDelete = (record: ISticker) => {
      try{
          deleteSticker(record.id)
          messageApi.success("삭제되었습니다.")
      } catch (e) {
          messageApi.error("에러가 발생하였습니다.")
      } finally {
          router.push("/sticker/list")
      }
  }

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };
  const hasSelected = selectedRowKeys.length > 0;

  const columns: ColumnsType<ISticker> = [
    {
      key: "action",
      width: 120,
      align: "center",
      render: (_value: unknown, record: ISticker) => {
        return (
          <span className="flex justify-center gap-2">
            <Link href={`/sticker/edit/${record.id}`} className="px-2 py-1 text-sm btn">
              수정
            </Link>
            <Popconfirm
              title="스티커를 삭제하시겠습니까?"
              onConfirm={() => onDelete(record)}
              okText="예"
              cancelText="아니오"
            >
              <a className="px-2 py-1 text-sm btn">삭제</a>
            </Popconfirm>
          </span>
        );
      },
    },
    {
      title: "스티커 ID",
      dataIndex: "id",
      width: 100,
        render: (value: string, record: ISticker) => {
            return (
                <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.id}</span>
          </span>
            );
        },
    },
    {
      title: "스티커 이름",
      dataIndex: "name",
      render: (value: string, record: ISticker) => {
        return (
          <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.name}</span>
          </span>
        );
      },
    },
    {
      title: "스티커 URL",
      dataIndex: "url",
      render: (value: string, record: ISticker) => {
          return (
              <span>
                <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.url}</span>
              </span>
          );
      },
    },
    {
      title: "스티커 이미지",
      dataIndex: "name",
      render: (value: string, record: ISticker) => {
          return (
              <span>
                <img src={record.url} height={100} width={100} className="px-2 py-1 mr-1 bg-gray-100 rounded" />
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
      {contextHolder}
      <DefaultTableBtn className="justify-between">
        <div>
          <span style={{ marginLeft: 8 }}>{hasSelected ? `${selectedRowKeys.length}건 선택` : ""}</span>
        </div>

        <div className="flex-item-list">
          <Button type="primary" onClick={() => router.push("/sticker/new")}>
            스티커등록
          </Button>
        </div>
      </DefaultTableBtn>

      <DefaultTable<ISticker>
        rowSelection={rowSelection}
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

export default React.memo(StickerList);
