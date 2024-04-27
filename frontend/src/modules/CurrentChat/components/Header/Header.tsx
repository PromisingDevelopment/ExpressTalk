import { Box, Typography, useTheme } from "@mui/material";
import React from "react";
import { Logo } from "../../../../components/Logo";
import { useAppSelector } from "hooks/redux";
import { BurgerMenu } from "../BurgerMenu";

interface HeaderProps {
  secondMemberLogin: string | undefined;
}

const Header: React.FC<HeaderProps> = ({ secondMemberLogin }) => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        display: "flex",
        gap: 1.5,
        alignItems: "center",
        height: 96,
        px: 4.875,
        bgcolor: "#1F274E",
        borderLeft: "1px solid #353F75",
        [theme.breakpoints.down(767)]: {
          height: 70,
          px: 1.5,
        },
      }}>
      <BurgerMenu />
      <Logo isMain size={52} />
      <Typography fontSize={20} textTransform="capitalize">
        {!secondMemberLogin && "User"}
        {secondMemberLogin && secondMemberLogin}
      </Typography>
    </Box>
  );
};

export { Header };
