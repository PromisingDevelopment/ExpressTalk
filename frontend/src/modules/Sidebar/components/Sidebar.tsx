import { styled } from "@mui/material";
import React from "react";
import { ChatList } from "./ChatList";
import { ChatModes } from "./ChatModes";
import { Header } from "./Header";
import { Search } from "./Search";
import { Wrapper } from "./Wrapper";

interface SidebarProps {}

const Sidebar: React.FC<SidebarProps> = () => {
  const [currentChatMode, setCurrentChatMode] = React.useState(0);
  const [filterChatsValue, setFilterChatsValue] = React.useState("");
  const headerRef = React.useRef<HTMLDivElement | null>(null);
  const chatModesRef = React.useRef<HTMLDivElement | null>(null);
  const searchRef = React.useRef<HTMLFormElement | null>(null);
  const [subtractedHeight, setSubtractedHeight] = React.useState(0);

  React.useEffect(() => {
    const setHeight = () => {
      setTimeout(() => {
        const headerHeight = headerRef.current?.offsetHeight;
        const chatModesHeight = chatModesRef.current?.offsetHeight;
        const searchHeight = searchRef.current?.offsetHeight;

        if (headerHeight && chatModesHeight && searchHeight) {
          setSubtractedHeight(headerHeight + chatModesHeight + searchHeight);
        }
      }, 0);
    };

    window.addEventListener("resize", setHeight);
    setHeight();
  });

  return (
    <>
      <Wrapper>
        <Header switchChatMode={setCurrentChatMode} ref={headerRef} />
        <SidebarContent>
          <ChatModes
            ref={chatModesRef}
            switchChatMode={setCurrentChatMode}
            currentChatMode={currentChatMode}
          />
          <ChatList
            subtractedHeight={subtractedHeight}
            currentChatMode={currentChatMode}
            filterChatsValue={filterChatsValue}
          />
          <Search ref={searchRef} setValue={setFilterChatsValue} />
        </SidebarContent>
      </Wrapper>
    </>
  );
};

const SidebarContent = styled("div")`
  display: flex;
  flex-direction: column;
  position: relative;
  flex-grow: 1;
  background: #1f274e;
`;

export { Sidebar };
