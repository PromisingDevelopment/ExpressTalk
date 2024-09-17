import { styled } from "@mui/material";
import LeftMessageTailImage from "assets/images/left-message-tail.svg";
import RightMessageTailImage from "assets/images/right-message-tail.svg";
import { getCurrentTimeString } from "helpers/getCurrentTimeString";
import React from "react";

interface MessageProps {
  content: string;
  createdAt?: string;
  isMine?: boolean;
  senderLogin?: string;
  isGroupMessage?: boolean;
  isSystemMessage?: boolean;
  //message: PrivateMessage
}

const Message: React.FC<MessageProps> = (props) => {
  const { isMine, createdAt, content, senderLogin, isGroupMessage, isSystemMessage } =
    props;

  const isLowContent = content.length < 20;
  const isShownLogin = Boolean(isGroupMessage && !isMine && senderLogin);
  //const isAnon = Boolean(content && !senderId);
  const isAnon = isSystemMessage;
  //console.log("isShownLogin", isShownLogin);
  //console.log(isGroupMessage, !isMine, senderLogin);

  return (
    <StyledMessageContainer isAnon={isAnon} isMine={isMine}>
      <StyledMessageWrapper
        isMine={isMine}
        isAnon={isAnon}
        isLowContent={isLowContent}
        isShownLogin={isShownLogin}>
        {isShownLogin && <StyledName>{senderLogin}</StyledName>}
        <StyledContent isAnon={isAnon}>{content}</StyledContent>
        {!isAnon && createdAt && (
          <StyledDate>{getCurrentTimeString(createdAt)}</StyledDate>
        )}
      </StyledMessageWrapper>
    </StyledMessageContainer>
  );
};

const StyledMessageContainer = styled("div")<{ isMine?: boolean; isAnon?: boolean }>(
  ({ theme, isMine, isAnon }) =>
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
      isAnon ? { alignItems: "center" } : {},
    ])
);

const StyledMessageWrapper = styled("div")<{
  isMine?: boolean;
  isLowContent?: boolean;
  isShownLogin?: boolean;
  isAnon?: boolean;
}>(({ theme, isMine, isLowContent, isShownLogin, isAnon }) =>
  theme.unstable_sx([
    {
      bgcolor: "primary.main",
      width: "fit-content",
      minWidth: 120,
      maxWidth: 348,
      p: ({ spacing }) => ({
        md: spacing(1, 6.25, 1, 2.5),
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
      : isAnon
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
    isLowContent && !isAnon
      ? {
          p: ({ spacing }) => ({
            md: spacing(1, 6.25, 1, 2.5),
            xs: spacing(1, 6, 1, 2),
          }),
        }
      : {},
    isShownLogin
      ? {
          pt: {
            md: 3,
            xs: 3,
          },
        }
      : {},
  ])
);

const StyledName = styled("span")(({ theme }) =>
  theme.unstable_sx({
    position: "absolute",
    top: 4,
    fontWeight: 500,
    fontSize: 14,
    color: "#9fa8da",
  })
);

const StyledContent = styled("p")<{ isAnon?: boolean }>(({ theme, isAnon }) =>
  theme.unstable_sx([
    {
      fontSize: isAnon ? { md: 14, xs: 14 } : { md: 18, xs: 16 },
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
