import { Box, Grid, IconButton, Paper, Typography } from "@mui/material"
import { Delete, Edit } from "@mui/icons-material"
import Space from "../../models/Space"

const SpaceView = ({ space, on_edit, on_delete }: { space?: Space, on_edit: () => void, on_delete: () => void }) =>
  <Paper sx={{ padding: "1.2rem", borderRadius: "1.2rem" }}>
    <Grid container direction="column">

      <Typography variant="h6" gutterBottom fontWeight="bold"> {space ? space.name : "..."} </Typography>

      <Grid container direction="row" alignItems="center">
        <Box display="flex" flexGrow="1" minWidth="0.6rem" />
        <Grid container direction="row-reverse" marginTop="0.6rem" spacing="0.3rem">
          <IconButton onClick={on_delete} disabled={!space}> <Delete /> </IconButton>
          <IconButton onClick={on_edit} disabled={!space}> <Edit /> </IconButton>
        </Grid>
      </Grid>

    </Grid>
  </Paper>

export default SpaceView 
