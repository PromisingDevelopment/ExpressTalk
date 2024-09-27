import { styled } from "@mui/material";
import LeftMessageTailImage from "assets/images/left-message-tail.svg";
import RightMessageTailImage from "assets/images/right-message-tail.svg";
import { getCurrentTimeString } from "helpers/getCurrentTimeString";
import React from "react";
import { IMessage } from "types/IMessage";

interface MessageProps {
  isMine?: boolean;
  messageObj: IMessage;
}

const Message: React.FC<MessageProps> = (props) => {
  const { messageObj, isMine } = props;

  const renderMessage = (messageObj: IMessage, isMine?: boolean) => {
    const { isSystemMessage, messageDto, privateMessageDetailsDto, groupMessageDetailsDto } = messageObj;
    const { content, createdAt, messageId } = messageDto;
    const { senderLogin, attachedFile, senderId } =
      !isSystemMessage && privateMessageDetailsDto ? privateMessageDetailsDto : groupMessageDetailsDto || {};

    return (
      <StyledMessageContainer isSystem={isSystemMessage} isMine={isMine}>
        <StyledMessageWrapper isMine={isMine} isSystem={isSystemMessage}>
          {!isSystemMessage && <StyledName>{senderLogin}</StyledName>}
          <StyledContent isSystem={isSystemMessage}>{content}</StyledContent>
          {!isSystemMessage && createdAt && <StyledDate>{getCurrentTimeString(createdAt)}</StyledDate>}
        </StyledMessageWrapper>
      </StyledMessageContainer>
    );
  };

  return renderMessage(messageObj, isMine);
};

const StyledMessageContainer = styled("div")<{ isMine?: boolean; isSystem?: boolean }>(
  ({ theme, isMine, isSystem }) =>
    theme.unstable_sx([
      {
        paddingX: 5,
        display: "flex",
        flexDirection: "column",

        [theme.breakpoints.down(767)]: {
          paddingX: 3,
        },
      },

      isMine ? { alignItems: "flex-end" } : {},
      isSystem ? { alignItems: "center" } : {},
    ])
);

const StyledMessageWrapper = styled("div")<{
  isMine?: boolean;
  isShownLogin?: boolean;
  isSystem?: boolean;
}>(({ theme, isMine, isSystem }) =>
  theme.unstable_sx([
    {
      bgcolor: "primary.main",
      minWidth: 120,
      maxWidth: 348,
      p: ({ spacing }) => ({
        md: spacing(0.5, 6.25, 1, 2.5),
        xs: spacing(1, 1, 3, 2),
      }),

      fontSize: 20,
      borderRadius: "10px 10px 10px 0",
      position: "relative",

      "::before": {
        content: "''",
        position: "absolute",
        width: 13,
        height: 10,
        bottom: 1,
        transform: "translateY(100%)",
      },
    },

    isMine
      ? {
          borderRadius: "10px 10px 0 10px",
          bgcolor: "#424d87",
          ml: 3,
          "::before": {
            right: -1,
            background: `url(${RightMessageTailImage}) 0 0 / auto no-repeat`,
          },
        }
      : isSystem
      ? {
          borderRadius: "10px",
          background: "rgba(53, 63, 117, .7 )",
          px: {
            md: 2,
            xs: 2,
          },
        }
      : {
          borderRadius: "10px 10px 10px 0",
          mr: 3,
          "::before": {
            left: 0,
            background: `url(${LeftMessageTailImage}) 0 0 / auto no-repeat`,
          },
        },
    isSystem
      ? {
          p: ({ spacing }) => ({
            xs: spacing(0.5, 1.5),
          }),
        }
      : {},
  ])
);

const StyledName = styled("span")(({ theme }) =>
  theme.unstable_sx({
    width: 1,
    display: "inline-block",
    fontSize: 14,
    lineHeight: 1.2,
    fontWeight: 500,
    color: "#9fa8da",
    textOverflow: "ellipsis",
    whiteSpace: "nowrap",
    overflow: "hidden",
  })
);

const StyledContent = styled("p")<{ isSystem?: boolean }>(({ theme, isSystem }) =>
  theme.unstable_sx([
    {
      fontSize: isSystem ? { xs: 14 } : { md: 18, xs: 16 },
      wordBreak: "break-all",
    },
  ])
);

const StyledDate = styled("span")<{ isMine?: boolean; isLowContent?: boolean }>(
  ({ theme, isMine, isLowContent }) =>
    theme.unstable_sx([
      {
        fontSize: 12,
        color: isMine ? "#7a85c2" : "#6A73A6",
        position: "absolute",
        right: 10,
        bottom: 5,
        pointerEvents: "none",
      },
      isLowContent
        ? {
            right: 10,
            bottom: 9,
          }
        : {},
    ])
);

export { Message };
