import { Box, Typography, styled } from "@mui/material";
import React from "react";
import { Logo } from "../../../../components/Logo";
import { BurgerMenu } from "../BurgerMenu";
import { useAppSelector } from "hooks/redux";
import { isCurrentGroupChat } from "helpers/isCurrentGroupChat";

interface HeaderProps {
  login?: string;
}

const Header: React.FC<HeaderProps> = ({ login }) => {
  const currentChat = useAppSelector((state) => state.currentChat.currentChat);
  const currentChatType = useAppSelector((state) => state.root.currentChatType);
  const currentGroupChat = useAppSelector((state) => state.currentChat.currentGroupChat);

  const privateLayout = (
    <>
      <Logo isMain size={52} />

      <Title>
        {!login && "User"}
        {login && login}
      </Title>
    </>
  );

  const groupLayout = currentGroupChat ? (
    <>
      <Logo isMain size={52} />
      <div>
        <Title>{currentGroupChat.name}</Title>
        <p>
          {currentGroupChat.members.length}{" "}
          {currentGroupChat.members.length === 1 ? "member" : "members"}
        </p>
      </div>
    </>
  ) : (
    <Title>Group</Title>
  );

  return (
    <HeaderWrapper>
      <BurgerMenu />

      {currentChatType === "privateChat" ? privateLayout : groupLayout}
    </HeaderWrapper>
  );
};

const Title = styled("h3")`
  font-size: 20px;
  text-transform: capitalize;
  @media (max-width: 767px) {
    font-size: 16px;
  }
`;

const HeaderWrapper = styled("div")(({ theme }) =>
  theme.unstable_sx({
    display: "flex",
    gap: 1.5,
    alignItems: "center",
    height: 96,
    px: 4.875,
    bgcolor: "#1F274E",
    borderLeft: "1px solid #353F75",
    [theme.breakpoints.down(767)]: {
      height: 70,
      px: 1.5,
    },
  })
);

export { Header };
