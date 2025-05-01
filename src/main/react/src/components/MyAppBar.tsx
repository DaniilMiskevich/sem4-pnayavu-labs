import { Menu } from "@mui/icons-material";
import { AppBar, Box, IconButton, SvgIcon, Toolbar, Typography, useTheme } from "@mui/material";
import { MouseEventHandler } from "react";

const MyAppBar = ({ onClick }: { onClick?: MouseEventHandler<HTMLDivElement> }) => {
  const theme = useTheme()

  return <>
    <AppBar>
      <Toolbar variant="dense" disableGutters onClick={onClick} sx={{ gap: "0.6rem", margin: "0.3rem 0.6rem" }}>
        <IconButton color="inherit" onClick={e => { e.stopPropagation(); alert() }}>
          <Menu />
        </IconButton>
        <SvgIcon fontSize="large"> <image width="100%" height="100%" href="/icon.svg" /> </SvgIcon>
        <Typography variant="h5" fontWeight="900"> reddit clone </Typography>
      </Toolbar>
    </AppBar>
    <Box sx={theme.mixins.toolbar} />
  </>;
}

export default MyAppBar
