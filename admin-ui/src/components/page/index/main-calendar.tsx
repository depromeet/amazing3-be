import { Badge, BadgeProps, Calendar, CalendarProps } from "antd";
import { Dayjs } from "dayjs";
import React from "react";

export interface ISchedule {
  type: string;
  content: string;
}

const getListData = (value: Dayjs) => {
  let listData: ISchedule[] = [];

  if(value.year() === 2024 && value.month() === 0) {
    switch (value.date()) {
      case 6:
      case 13:
        listData = [
          { type: "success", content: "개별 팀 활동" },
        ];
        break;
      case 20:
      case 21:
        listData = [
          { type: "success", content: "강원도 여행!" },
        ];
        break;
      case 27:
        listData = [
          { type: "success", content: "QA, 최종 검수" },
        ];
        break;
      case 2:
      case 9:
      case 16:
      case 23:
      case 30:
        listData = [
          { type: "warning", content: "정규 회의" },
        ];
        break;
    }
  }

  if(value.year() === 2024 && value.month() === 1) {
    switch (value.date()) {
      case 3:
        listData = [
          { type: "success", content: "런칭 데이" },
        ];
        break;
      case 10:
        listData = [
          { type: "success", content: "개별 팀 활동" },
        ];
        break;
      case 17:
        listData = [
          { type: "success", content: "최종 발표" },
        ];
        break;
      case 6:
      case 13:
        listData = [
          { type: "warning", content: "정규 회의" },
        ];
        break;
    }
  }
  return listData || [];
};

const getMonthData = (value: Dayjs) => {
  return null
};

const MainCalendar = () => {
  const monthCellRender = (value: Dayjs) => {
    const num = getMonthData(value);
    return num ? (
      <div className="notes-month">
        <section>{num}</section>
        <span>Backlog number</span>
      </div>
    ) : null;
  };

  const dateCellRender = (value: Dayjs) => {
    const listData = getListData(value);
    return (
      <ul className="events">
        {listData.map((item) => (
          <li key={item.content}>
            <Badge status={item.type as BadgeProps["status"]} text={item.content} />
          </li>
        ))}
      </ul>
    );
  };

  const cellRender: CalendarProps<Dayjs>['cellRender'] = (current, info) => {
    if (info.type === 'date') return dateCellRender(current);
    if (info.type === 'month') return monthCellRender(current);
    return info.originNode;
  };

  return <Calendar cellRender={cellRender} />;
};

export default React.memo(MainCalendar);
