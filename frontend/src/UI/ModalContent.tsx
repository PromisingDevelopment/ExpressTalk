import { IconButton, Typography, styled, useTheme } from "@mui/material";
import React from "react";
import CloseIcon from "@mui/icons-material/CloseRounded";

interface ModalContentProps {
  children: React.ReactNode;
  onCloseModal: any;
  title: string;
}

const ModalContent = React.forwardRef<HTMLDivElement, ModalContentProps>(
  ({ children, onCloseModal, title }, ref) => {
    const { breakpoints } = useTheme();

    return (
      <CenteredWrapper ref={ref} tabIndex={-1}>
        <StyledWrapper>
          <StyledContainer>
            <Typography
              variant="h4"
              sx={{
                mb: 6,
                [breakpoints.down("sm")]: {
                  fontSize: 30,
                  mb: 4,
                },
                [breakpoints.down(450)]: {
                  fontSize: 24,
                },
                [breakpoints.down(400)]: {
                  fontSize: 20,
                },
              }}>
              {title}
            </Typography>
            {children}
            <CloseButton onClick={onCloseModal}>
              <CloseIcon sx={{ color: "#fff", fontSize: 40 }} />
            </CloseButton>
          </StyledContainer>
        </StyledWrapper>
      </CenteredWrapper>
    );
  }
);

const StyledContainer = styled("div")(({ theme: { breakpoints, spacing } }) => ({
  px: spacing(2),
  [breakpoints.down(450)]: {
    px: spacing(1),
  },
}));

const StyledWrapper = styled("div")(
  ({ theme: { spacing, shape, breakpoints, palette } }) => ({
    paddingLeft: spacing(2),
    paddingRight: spacing(2),
    background: palette.primary.dark,
    position: "relative",
    padding: spacing(4, 8, 8),
    borderRadius: shape.borderRadius * 3,
    [breakpoints.down("sm")]: {
      padding: spacing(4, 2, 5),
      borderRadius: shape.borderRadius * 1,
    },
    [breakpoints.down(450)]: {
      padding: spacing(4, 1, 5),
    },
  })
);

const CenteredWrapper = styled("div")(({ theme: { spacing } }) => ({
  position: "absolute",
  top: "50%",
  left: "50%",
  maxWidth: 600,
  width: "100%",
  transform: "translate(-50%, -50%)",
  padding: spacing(0, 2),
  "@media (max-height: 400px)": {
    position: "static",
    maxWidth: "100%",
    paddingTop: 30,
    paddingBottom: 30,
    transform: "translate(0, 0)",
    overflowY: "auto",
    height: "100%",
    display: "flex",
    justifyContent: "center",
    alignItems: "start",
  },
}));

const CloseButton = styled(IconButton)`
  position: absolute;
  top: 20px;
  right: 20px;
  @media (max-width: 400px) {
    right: 5px;
    top: 15px;

    svg {
      font-size: 35px;
    }
  }
`;
export default ModalContent;
