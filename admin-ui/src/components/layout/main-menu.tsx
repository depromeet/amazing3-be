import { Divider } from "antd";
import { Home, Monitor, Package2 } from "lucide-react";
import React from "react";
import Menu, { IMenu } from "./nav";

const mainMenuData: IMenu[] = [
  {
    id: "home",
    name: "홈",
    icon: <Home className="w-5 h-5" />,
    link: {
      path: "/",
    },
  },
  {
    id: "tag",
    name: "태그 관리",
    icon: <Package2 className="w-5 h-5" />,
    submenu: [
      {
        id: "tagList",
        name: "태그 목록",
        link: {
          path: "/tag/list",
        },
      },
    ],
  },
  {
    id: "sticker",
    name: "스티커 관리",
    icon: <Package2 className="w-5 h-5" />,
    submenu: [
      {
        id: "stickerList",
        name: "스티커 목록",
        link: {
          path: "/sticker/list",
        },
      },
    ],
  }
];

const devMenuData: IMenu[] = [
  {
    id: "dev",
    name: "사용 가이드",
    icon: <Monitor className="w-5 h-5" />,
    submenu: [
      {
        name: "폼",
        link: {
          path: "/sample/form",
        },
      },
      {
        name: "반디부디 폼",
        link: {
          path: "/sample/bandiboodi-form",
        },
      }
    ],
  },
];

const MainMenu = () => {
  return (
    <>
      <>
        <Divider orientation="left" plain>
          메인
        </Divider>

        <Menu data={mainMenuData} />
      </>
      <>
        <Divider orientation="left" plain>
          개발
        </Divider>

        <Menu data={devMenuData} />
      </>
    </>
  );
};

export default React.memo(MainMenu);
