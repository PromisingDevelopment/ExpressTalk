import React from "react";
import MoreIcon from "@mui/icons-material/MoreVert";
import { Box, Menu, MenuItem, Modal, styled, TextField, Typography } from "@mui/material";
import { useAppSelector } from "hooks/redux";
import CustomIconButton from "UI/CustomIconButton";
import { ModalLayout } from "components/ModalLayout";
import { getMemberByLogin } from "axios/getMemberByLogin";
import { addGroupMember, editGroupName, leaveGroup, removeGroupMember } from "wsConfig";

interface MemberActionsProps {}

const MemberActions: React.FC<MemberActionsProps> = () => {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [menuOpen, setMenuOpen] = React.useState(false);
  const groupId = useAppSelector((state) => state.root.currentChatId);

  const [modalOpenIndex, setModalOpenIndex] = React.useState<number | null>(null);

  const closeMenu = () => {
    setMenuOpen(false);
  };
  const openMenu = (e: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(e.currentTarget);
    setMenuOpen(true);
  };
  const addOrRemoveUser = async (login: string, actionType: "add" | "remove") => {
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
  const onLeaveGroup = () => {
    if (groupId) {
      leaveGroup(groupId);
    }
  };

  const onRemoveGroup = () => {};

  const onMenuItemClick = (e: React.MouseEvent, index: number) => {
    (e.currentTarget as HTMLElement).blur();
    setModalOpenIndex(index);
  };

  const actionsData = [
    {
      label: "Add a new member",
      buttonLabel: "Add member",
      inputLabel: "Input a member login",
      inputName: "input-add-member-login",
      onSubmit: (login: string) => addOrRemoveUser(login, "add"),
    },
    {
      label: "Remove a member",
      buttonLabel: "Remove member",
      inputLabel: "Input a member login",
      inputName: "input-remove-member-login",
      onSubmit: (login: string) => addOrRemoveUser(login, "remove"),
    },
    {
      label: "Edit group name",
      buttonLabel: "Edit group name",
      inputLabel: "Input new name of the group",
      inputName: "input-name-group",
      onSubmit: (groupName: string) => onEditGroupName(groupName),
    },
    {
      label: "Are you sure you want to LEAVE the group",
      buttonLabel: "Leave the group",
      onSubmit: onLeaveGroup,
      withoutInput: true,
    },
    {
      label: "Are you sure you want to REMOVE the group",
      buttonLabel: "Remove the group",
      onSubmit: onRemoveGroup,
      withoutInput: true,
    },
  ];

  return (
    <>
      <CustomIconButton Icon={MoreIcon} label="Show actions" onClick={openMenu} />
      <Menu open={menuOpen} onClose={closeMenu} anchorEl={anchorEl}>
        {actionsData.map((actionData, i) => (
          <MenuItem onClick={(e) => onMenuItemClick(e, i)}>{actionData.buttonLabel}</MenuItem>
        ))}
      </Menu>
      {actionsData.map((actionData, i) => (
        <ModalLayout
          stateWithIndex={{ setValue: setModalOpenIndex, value: modalOpenIndex === i }}
          closeMenu={closeMenu}
          {...actionData}
        />
      ))}
    </>
  );
};

export { MemberActions };
