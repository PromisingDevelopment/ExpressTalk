import { styled } from "@mui/material";
import React from "react";
import { GroupChatMember } from "types/GroupChat";

interface GroupMembersInfoProps {
  members: GroupChatMember[];
}

const GroupMembersInfo: React.FC<GroupMembersInfoProps> = ({ members }) => {
  const memberLen = members.length;

  return (
    <StyledWrapper>
      <span>
        {memberLen} {memberLen === 1 ? "member" : "members"}
      </span>
      <PopoverWrapper className="header-group-popover">
        <Popover>
          {members.map((member) => (
            <PopoverItem key={member.id}>
              <h4>{member.login}</h4>
              <span>{member.status}</span>
            </PopoverItem>
          ))}
        </Popover>
      </PopoverWrapper>
    </StyledWrapper>
  );
};

const StyledWrapper = styled("div")`
  position: relative;
  width: fit-content;
  &:hover {
    .header-group-popover {
      opacity: 1;
      pointer-events: all;
    }
  }
`;

const PopoverWrapper = styled("div")`
  width: 200px;
  position: absolute;
  z-index: 1000;
  transform: translateX(-46%);
  opacity: 0;
  transition: opacity 0.3s ease 0s;
  pointer-events: none;

  padding-top: 30px;

  @media (max-width: 767px) {
    top: 80px;
  }
`;

const Popover = styled("div")`
  background: #1f274e;
  border-radius: 8px;
  padding-top: 12px;
  padding-bottom: 12px;
  color: #fff;
`;

const PopoverItem = styled("div")`
  padding: 5px 10px;
  display: flex;

  justify-content: space-between;
  &:not(:last-child) {
    border-bottom: 1px solid #353f75;
  }
`;

export { GroupMembersInfo };
