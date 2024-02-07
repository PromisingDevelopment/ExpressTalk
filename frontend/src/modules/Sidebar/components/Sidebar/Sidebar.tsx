import React from "react";
import { Wrapper } from "../Wrapper";
import { Header } from "../Header";
import { Chats } from "../Chats";

interface SidebarProps {}

const Sidebar: React.FC<SidebarProps> = () => {
  return (
    <Wrapper>
      <Header />
      <Chats />
    </Wrapper>
  );
};

export { Sidebar };
