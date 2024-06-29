import { styled } from "@mui/material";
import { Logo } from "components/Logo";
import { useAppSelector } from "hooks/redux";
import React from "react";
import { BurgerMenu } from "./BurgerMenu";
import { GroupMembersInfo } from "./GroupMembersInfo";
import { MemberActions } from "./MemberActions";

interface HeaderProps {
  login?: string;
}

const Header: React.FC<HeaderProps> = ({ login }) => {
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

  const groupLayout = currentGroupChat && (
    <>
      <Logo isMain size={52} />

      <div style={{ flexGrow: 1 }}>
        <Title>{currentGroupChat.name}</Title>
        <GroupMembersInfo members={currentGroupChat.members} />
      </div>
      <MemberActions />
    </>
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
    position: "relative",
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
