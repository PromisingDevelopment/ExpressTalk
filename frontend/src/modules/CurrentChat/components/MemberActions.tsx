import MoreIcon from "@mui/icons-material/MoreVert";
import PersonAddIcon from "@mui/icons-material/PersonAddAlt1";
import PersonRemoveIcon from "@mui/icons-material/PersonRemove";
import { Menu, MenuItem, styled } from "@mui/material";
import { getMemberByLogin } from "axios/getMemberByLogin";
import { ModalLayout } from "components/ModalLayout";
import { useAppSelector } from "hooks/redux";
import React from "react";
import CustomIconButton from "UI/CustomIconButton";
import { addGroupMember, removeGroupMember } from "wsConfig";

interface MemberActionsProps {}

const MemberActions: React.FC<MemberActionsProps> = () => {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [open, setOpen] = React.useState(false);
  const groupId = useAppSelector((state) => state.root.currentChatId);

  const closeMenu = () => {
    setOpen(false);
  };
  const openMenu = (e: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(e.currentTarget);
    setOpen(true);
  };

  const onSubmit = async (login: string, actionType: "add" | "remove") => {
    const { id: memberId } = await getMemberByLogin(login);

    if (memberId && groupId) {
      if (actionType === "add") {
        addGroupMember(groupId, memberId);
      } else {
        removeGroupMember(groupId, memberId);
      }
    }
  };

  return (
    <>
      <CustomIconButton Icon={MoreIcon} label="Show actions" onClick={openMenu} />
      <Menu open={open} onClose={closeMenu} anchorEl={anchorEl}>
        <MenuItem>
          <ModalLayout
            withoutIcon
            Icon={PersonAddIcon}
            inputLabel="Input a member login"
            inputName="input-member-login"
            label="Add a new member"
            onSubmit={(login: string) => onSubmit(login, "add")}
          />
        </MenuItem>
        <MenuItem>
          <ModalLayout
            withoutIcon
            Icon={PersonRemoveIcon}
            inputLabel="Input a member login"
            inputName="input-member-login"
            label="Remove a member"
            onSubmit={(login: string) => onSubmit(login, "remove")}
          />
        </MenuItem>
      </Menu>
    </>
  );
};

const StyledWrapper = styled("div")`
  display: flex;
  gap: 12px;
`;

export { MemberActions };
