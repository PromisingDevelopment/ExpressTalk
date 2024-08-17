import AddRoleIcon from "@mui/icons-material/FaceRetouchingNatural";
import { Modal, styled } from "@mui/material";
import { FormControlLabel, Radio, RadioGroup } from "@mui/material";
import React from "react";
import { GroupChatMember } from "types/GroupChat";
import { MemberRoles } from "types/MemberRoles";
import CustomIconButton from "UI/CustomIconButton";
import ModalContent from "UI/ModalContent";
import { SubmitButton } from "UI/SubmitButton";
import { setMemberRole } from "wsConfig";

interface GroupMembersInfoProps {
  members: GroupChatMember[];
  chatId: string;
}

const GroupMembersInfo: React.FC<GroupMembersInfoProps> = ({ members, chatId }) => {
  const [open, setOpen] = React.useState(false);
  const memberLen = members.length;
  const [isDisabled, setIsDisabled] = React.useState(false);
  const [roleValue, setRoleValue] = React.useState(MemberRoles.MEMBER);

  const closeModal = () => {
    setOpen(false);
  };
  const openModal = () => {
    setOpen(true);
  };

  const onChangeRole = (e: React.ChangeEvent<HTMLInputElement>) => {
    const target = e.target;

    const value: any = target.value;

    setRoleValue(value);
  };

  const handleSubmit = async (
    e: React.FormEvent<HTMLFormElement>,
    userToGiveRoleId: string
  ) => {
    e.preventDefault();
    setIsDisabled(true);

    setMemberRole(chatId, userToGiveRoleId, roleValue);

    setIsDisabled(false);
    closeModal();
  };

  React.useEffect(() => {
    console.log("group members", members);
  }, [members]);

  return (
    <StyledWrapper>
      <span>
        {memberLen} {memberLen === 1 ? "member" : "members"}
      </span>
      <PopoverWrapper className="header-group-popover">
        <Popover>
          {members.map((member) => (
            <PopoverItem key={member.id}>
              <h4>{member.user.login}</h4>
              <span>{member.user.status}</span>
              <CustomIconButton
                isSmall
                label="add role"
                Icon={AddRoleIcon}
                onClick={openModal}
              />
              <Modal onClose={closeModal} open={open}>
                <ModalContent
                  isSmallMargin
                  title={"Set a role to " + member.user.login}
                  onCloseModal={closeModal}>
                  <form onSubmit={(e) => handleSubmit(e, member.user.id)}>
                    <StyledRadioGroup value={roleValue} onChange={onChangeRole}>
                      <StyledFormControlLabel
                        value={MemberRoles.MEMBER}
                        control={<Radio color="default" />}
                        label="Member"
                      />
                      <StyledFormControlLabel
                        value={MemberRoles.ADMIN}
                        control={<Radio color="default" />}
                        label="Admin"
                      />
                    </StyledRadioGroup>
                    <SubmitButton disabled={isDisabled} label="Set the role" />
                  </form>
                </ModalContent>
              </Modal>
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
  color: #fff;
`;

const PopoverItem = styled("div")`
  padding: 5px 10px;
  display: flex;
  align-items: center;

  justify-content: space-between;
  &:not(:last-child) {
    border-bottom: 1px solid #353f75;
  }
`;

const StyledFormControlLabel = styled(FormControlLabel)`
  span {
    font-size: 20px;
  }
`;

const StyledRadioGroup = styled(RadioGroup)(({ theme }) =>
  theme.unstable_sx({
    width: {
      xs: "100%",
      sm: 350,
    },
  })
);

export { GroupMembersInfo };
