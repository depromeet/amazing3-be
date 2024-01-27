import { IDashboardResponse } from "@/client/dashboard";
import { ArrowUp } from "lucide-react";
import React from "react";
import CountUp from "react-countup";
import {Divider} from "antd";

interface IDashboardStaticsProps {
  data: IDashboardResponse;
}

const renderTodayIncrease = (value: number, unit: string) => {
  if (0 < value) {
    return (
      <span className="flex items-center px-2 py-1 text-sm text-white rounded-full bg-emerald">
        <ArrowUp className="w-5 h-4" />
        오늘 {value}{unit} 추가
      </span>
    );
  }
};

const DashboardStatistics = ({ data }: IDashboardStaticsProps) => {
  return (
      <>
        <div className="grid grid-cols-1 gap-5 md:grid-cols-2 lg:grid-cols-3">
          <div className="p-5 border rounded-lg ">
            <div>유저</div>
            <div className="mt-3">
              <div className="flex items-center mt-3">
                <div className="text-2xl font-semibold grow">
                  <CountUp end={data.body.userStatics.total} separator=","/>명
                </div>
                <div>{renderTodayIncrease(data.body.userStatics.todayIncrease, '명')}</div>
              </div>
            </div>
          </div>
          <div className="p-5 border rounded-lg ">
            <div>목표</div>
            <div className="flex items-center mt-3">
              <div className="text-2xl font-semibold grow">
                <CountUp end={data.body.goalStatics.total} separator=","/>건
              </div>
              <div>{renderTodayIncrease(data.body.goalStatics.todayIncrease, '건')}</div>
            </div>
          </div>
          <div className="p-5 border rounded-lg ">
            <div>세부 목표</div>
            <div className="flex items-center mt-3">
              <div className="text-2xl font-semibold grow">
                <CountUp end={data.body.taskStatics.total} separator=","/>건
              </div>
              <div>{renderTodayIncrease(data.body.taskStatics.todayIncrease, '건')}</div>
            </div>
          </div>
        </div>
        <Divider/>
        <div className="grid grid-cols-1 gap-5 md:grid-cols-2 lg:grid-cols-3">
          <div className="p-5 border rounded-lg ">
            <div>오늘 접속 유저 비율</div>
            <div className="mt-3">
              <div className="flex items-center mt-3">
                <div className="text-2xl font-semibold grow">
                  {data.body.activeUserStatics.perTodayPercent}%
                </div>
              </div>
            </div>
          </div>
          <div className="p-5 border rounded-lg ">
            <div>이번 주 접속 유저 비율</div>
            <div className="flex items-center mt-3">
              <div className="text-2xl font-semibold grow">
                {data.body.activeUserStatics.perWeekPercent}%
              </div>
            </div>
          </div>
          <div className="p-5 border rounded-lg ">
            <div>이번 달 접속 유저 비율</div>
            <div className="flex items-center mt-3">
              <div className="text-2xl font-semibold grow">
                {data.body.activeUserStatics.perMonthPercent}%
              </div>
            </div>
          </div>
        </div>
      </>
  );
};

export default React.memo(DashboardStatistics);
