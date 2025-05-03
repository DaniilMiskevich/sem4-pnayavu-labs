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
      <TableCell> Spectres </TableCell>
      <TableCell>
        <IconButton disabled> <Icon /> </IconButton>
        <IconButton disabled> <Icon /> </IconButton>
      </TableCell>
    </TableRow>
  </TableHead>

  <TableBody>
    {sparks.sort((a, b) => a.id - b.id).map(spark =>
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

export default SparkTable 
