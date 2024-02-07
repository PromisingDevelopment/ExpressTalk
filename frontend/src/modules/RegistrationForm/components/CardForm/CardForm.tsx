import { Box, useTheme } from "@mui/material";
import React from "react";

interface CardFormProps {
  children: React.ReactNode;
}

const CardForm: React.FC<CardFormProps> = ({ children }) => {
  const theme = useTheme();
  const cardFormRef = React.useRef<HTMLDivElement>();
  const [isMediaStyles, setIsMediaStyles] = React.useState(false);

  React.useEffect(() => {
    const cardForm = cardFormRef.current;

    if (!cardForm) return;

    const onResizeHandle = () => {
      const formHeight = cardForm.clientHeight;
      const bodyHeight = document.body.clientHeight;

      setIsMediaStyles(formHeight + 60 > bodyHeight);
    };

    window.onresize = onResizeHandle;

    onResizeHandle();
  }, []);

  return (
    <Box
      sx={[
        {
          height: "100vh",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          px: 2,
        },
        isMediaStyles && {
          alignItems: "flex-start",
          py: 6,
          height: "auto",
        },
      ]}>
      <Box
        ref={cardFormRef}
        sx={{
          borderRadius: 6,
          background: (theme) => theme.palette.primary.dark,
          maxWidth: 588,
          width: 1,
          p: 8,
          textAlign: "center",
          [theme.breakpoints.down("sm")]: {
            maxWidth: 1,
            py: 8,
            px: 4,
          },
        }}>
        <Box sx={{ position: "relative" }}>{children}</Box>
      </Box>
    </Box>
  );
};

export { CardForm };
