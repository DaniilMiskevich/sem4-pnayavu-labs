import SpectresView from "../SpectresView"
import Spark from "../../models/Spark"
import { Box, Grid, IconButton, Paper, Typography } from "@mui/material"
import { Delete, Edit } from "@mui/icons-material"

const SparkView = ({ spark, on_edit, on_delete }: { spark?: Spark, on_edit: () => void, on_delete: () => void }) =>
  <Paper sx={{ padding: "1.2rem", borderRadius: "1.2rem" }}>
    <Grid container direction="column">

      <Typography variant="h6" gutterBottom fontWeight="bold">{spark ? spark.name : "..."}</Typography>
      <SpectresView names={spark?.spectre_names} />

      <Grid container direction="row" alignItems="center">
        <Typography variant="subtitle2">by {spark ? "anonnymous user" : "..."}</Typography>
        <Box display="flex" flexGrow="1" minWidth="0.6rem" />
        <Grid container direction="row-reverse" marginTop="0.6rem" spacing="0.3rem">
          <IconButton onClick={on_delete} disabled={!spark}> <Delete /> </IconButton>
          <IconButton onClick={on_edit} disabled={!spark}> <Edit /> </IconButton>
        </Grid>
      </Grid>

    </Grid>
  </Paper>

export default SparkView 
