import Spark from "../../models/Spark"
import { Icon, IconButton, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material"
import { Delete, Edit } from "@mui/icons-material"

const SparkTable = ({ sparks, on_edit, on_delete }: {
  sparks: Spark[],
  on_edit: (id: number) => void,
  on_delete: (id: number) => void
}) => <TableContainer> <Table>
  <TableHead>
    <TableRow>
      <TableCell> Name </TableCell>
      <TableCell align="right"> Spectres </TableCell>
      <TableCell>
        <IconButton disabled> <Icon /> </IconButton>
        <IconButton disabled> <Icon /> </IconButton>
      </TableCell>
    </TableRow>
  </TableHead>

  <TableBody>
    {sparks.map(spark =>
      <TableRow key={spark.id}>
        <TableCell> {spark.name} </TableCell>
        <TableCell> {spark.spectre_names.join(", ")} </TableCell>
        <TableCell align="right">
          <IconButton onClick={() => on_edit(spark.id)}> <Edit /> </IconButton>
          <IconButton onClick={() => on_delete(spark.id)}> <Delete /> </IconButton>
        </TableCell>
      </TableRow>)}
  </TableBody>
</Table> </TableContainer>

// <Paper sx={{ padding: "1.2rem" }}>
//   <Grid container direction="column">
//
//     <Typography variant="h6" gutterBottom fontWeight="bold">{spark ? spark.name : "..."}</Typography>
//     <SpectresView names={spark?.spectre_names} />
//
//     <Grid container direction="row" alignItems="center">
//       <Typography variant="subtitle2">by {spark ? "anonnymous user" : "..."}</Typography>
//       <Box display="flex" flexGrow="1" minWidth="0.6rem" />
//       <Grid container direction="row-reverse" marginTop="0.6rem" spacing="0.3rem">
//         <IconButton onClick={on_delete} disabled={!spark}> <Delete /> </IconButton>
//         <IconButton onClick={on_edit} disabled={!spark}> <Edit /> </IconButton>
//       </Grid>
//     </Grid>
//
//   </Grid>
// </Paper>

export default SparkTable 
