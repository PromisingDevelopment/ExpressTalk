import React from "react";
import { Wrapper } from "../Wrapper";
import { Header } from "../Header";
import { SidebarContent } from "../SidebarContent";

interface SidebarProps {}

const Sidebar: React.FC<SidebarProps> = () => {
  return (
    <>
      <Wrapper>
        <Header />
        <SidebarContent />
      </Wrapper>
    </>
  );
};

export { Sidebar };
