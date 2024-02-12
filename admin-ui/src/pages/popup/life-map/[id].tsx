import { getDefaultLayout, IDefaultLayoutPage, IPageHeader } from "@/components/layout/default-layout";
import { useRouter } from "next/router";
import {IGoal, ITask, useLifeMap} from "@/client/life-map";
import DefaultTable from "@/components/shared/ui/default-table";
import React from "react";
import {ColumnsType} from "antd/es/table";

const pageHeader: IPageHeader = {
    title: "인생 지도",
};

const LifeMapPopup: IDefaultLayoutPage = () => {
    const router = useRouter();
    const { data, error, isLoading, isValidating } = useLifeMap(router.query.id as string);

    const formattingTask = (task: ITask): string => {
        if(task.isTaskDone) {
            return '✅ ' + task.taskDescription + ' \n  \n'
        }

        return '❎ '  + task.taskDescription + ' \n \n';
    }

    const columns: ColumnsType<IGoal> = [
        {
            title: "목표 명",
            dataIndex: "title",
            width: 100,
            render: (value: string, record: IGoal) => {
                return (
                    <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.title}</span>
          </span>
                );
            },
        },
        {
            title: "설명",
            dataIndex: "description",
            width: 100,
            render: (value: string, record: IGoal) => {
                return (
                    <>
                    <span>
                      <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.description}</span>
                    </span>
                    </>
                );
            },
        },
        {
            title: "마감일",
            dataIndex: "deadline",
            width: 100,
            render: (value: string, record: IGoal) => {
                return (
                    <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.deadline}</span>
          </span>
                );
            },
        },
        {
            title: "태그 명",
            dataIndex: "tagContent",
            width: 100,
            render: (value: string, record: IGoal) => {
                return (
                    <span>
            <span className="px-2 py-1 mr-1 bg-gray-100 rounded">{record.tagInfo.tagContent}</span>
          </span>
                );
            },
        },
        {
            title: "세부 목표",
            dataIndex: "tasks",
            width: 100,
            render: (value: string, record: IGoal) => {
                return (
                    <span>
                        { record.tasks.map((task, index) => (
                            <>
                                <span className=" rounded border-b-2">{formattingTask(task)}</span>
                                <br/>
                            </>
                        ))}
                    </span>
                );
            },
        },
    ];

    return (
        <>
            <div className="flex items-center text-2xl text-gray-900 mt-5">🚪 공개 여부: {data?.body.isPublic === true ? '공개' : '비공개'}</div>
            <div className="flex items-center text-2xl text-gray-900 mt-5">😘 응원 받은 수: {data?.body.cheeringCount}</div>
            <div className="flex items-center text-2xl text-gray-900 mt-5">🔥 방문자 수: {data?.body.viewCount}</div>
            <div className="flex items-center text-2xl text-gray-900 mt-5">🚩 목표 리스트</div>
            <DefaultTable<IGoal>
                columns={columns}
                dataSource={data?.body.goals || []}
                loading={isLoading}
                className="mt-3"
                countLabel={data?.body.goals.length}
            />
        </>
    )
};

LifeMapPopup.getLayout = getDefaultLayout;
LifeMapPopup.pageHeader = pageHeader;
LifeMapPopup.setSidebarClose = true;

export default LifeMapPopup;
