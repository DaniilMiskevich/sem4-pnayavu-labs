import { Collections, Home, Menu } from "@mui/icons-material";
import { AppBar, Box, Button, Drawer, Grid, IconButton, SvgIcon, Toolbar, Typography, useTheme } from "@mui/material";
import { useState } from "react";

const MyAppBar = ({ on_navigate }: { on_navigate: (uri: string) => void }) => {
  const theme = useTheme()

  const [is_drawer_open, set_is_drawer_open] = useState(false)

  return <>
    <Drawer open={is_drawer_open} onClose={() => set_is_drawer_open(false)}>
      <Box width="300px" onClick={() => set_is_drawer_open(false)}>
        <Grid container direction="column" margin="1.2rem">
          {[
            { uri: "/", label: "Home", icon: <Home /> },
            { uri: "/spaces", label: "All Spaces", icon: <Collections /> },
          ].map(item => <Button size="large" sx={{ padding: "0.6rem", justifyContent: "flex-start" }}
            startIcon={item.icon} onClick={() => on_navigate(item.uri)}>
            {item.label}
          </Button>)}
        </Grid>
      </Box>
    </Drawer >

    <AppBar>
      <Toolbar variant="dense" disableGutters onClick={() => on_navigate("/")} sx={{ gap: "0.6rem", margin: "0.3rem 0.6rem" }}>
        <IconButton size="large" color="inherit" onClick={e => { e.stopPropagation(); set_is_drawer_open(true) }}>
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
