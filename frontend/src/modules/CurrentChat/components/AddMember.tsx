import PersonAddIcon from "@mui/icons-material/PersonAdd";
import { getMemberByLogin } from "axios/getMemberByLogin";
import { ModalLayout } from "components/ModalLayout";
import { useAppSelector } from "hooks/redux";
import React from "react";
import { addGroupMember } from "wsConfig";

interface AddMemberProps {}

const AddMember: React.FC<AddMemberProps> = () => {
  const groupId = useAppSelector((state) => state.root.currentChatId);

  const onSubmit = async (login: string) => {
    const { id: memberId } = await getMemberByLogin(login);

    if (memberId && groupId) {
      addGroupMember(groupId, memberId);
    }
  };

  return (
    <ModalLayout
      Icon={PersonAddIcon}
      inputLabel="Input a member login"
      inputName="input-member-login"
      label="Add a new member"
      onSubmit={onSubmit}
    />
  );
};

export { AddMember };
