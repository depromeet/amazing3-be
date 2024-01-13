import useSWR from "swr";

export interface IDashboard {
  userStatics: {
    total: number;
    todayIncrease: number;
  };
  goalStatics: {
    total: number;
    todayIncrease: number;
  };
  taskStatics: {
    total: number;
    todayIncrease: number;
  };
}

export interface IDashboardResponse {
  result: number;
  body: IDashboard
  error: {
    code: string;
    message: string;
    data: object;
  };
}
export const useDashboard = () => {
  return useSWR<IDashboardResponse>(`dashboard`);
};
