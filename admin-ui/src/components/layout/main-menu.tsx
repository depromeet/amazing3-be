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
  },
  {
    id: "defaultProfile",
    name: "기본 프로필 관리",
    icon: <Package2 className="w-5 h-5" />,
    submenu: [
      {
        id: "defaultProfileList",
        name: "기본 프로필 목록",
        link: {
          path: "/default-profile/list",
        },
      },
    ],
  },
  {
    id: "user",
    name: "유저 관리",
    icon: <Package2 className="w-5 h-5" />,
    submenu: [
      {
        id: "userList",
        name: "유저 목록",
        link: {
          path: "/user/list",
        },
      },
    ],
  }
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
    </>
  );
};

export default React.memo(MainMenu);
