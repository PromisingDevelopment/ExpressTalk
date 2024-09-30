import MoreIcon from "@mui/icons-material/MoreVert";
import { Menu, MenuItem } from "@mui/material";
import { getMemberByLogin } from "axios/getMemberByLogin";
import { ModalLayout } from "components/ModalLayout";
import { useAppSelector } from "hooks/redux";
import React from "react";
import CustomIconButton from "UI/CustomIconButton";
import { addGroupMember, editGroupName, removeGroupMember } from "wsConfig";

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

  const onEditGroupName = (groupName: string) => {
    if (groupId) {
      editGroupName(groupId, groupName);
    }
  };

  return (
    <>
      <CustomIconButton Icon={MoreIcon} label="Show actions" onClick={openMenu} />
      <Menu open={open} onClose={closeMenu} anchorEl={anchorEl}>
        <MenuItem>
          <ModalLayout
            withoutIcon
            closeMenu={closeMenu}
            inputLabel="Input a member login"
            inputName="input-member-login"
            label="Add a new member"
            onSubmit={(login: string) => onSubmit(login, "add")}
          />
        </MenuItem>
        <MenuItem>
          <ModalLayout
            withoutIcon
            closeMenu={closeMenu}
            inputLabel="Input a member login"
            inputName="input-member-login"
            label="Remove a member"
            onSubmit={(login: string) => onSubmit(login, "remove")}
          />
        </MenuItem>
        <MenuItem>
          <ModalLayout
            withoutIcon
            closeMenu={closeMenu}
            inputLabel="Input new name of the group"
            inputName="input-name-group"
            label="Edit group name"
            onSubmit={(groupName: string) => onEditGroupName(groupName)}
          />
        </MenuItem>
      </Menu>
    </>
  );
};

export { MemberActions };
