import { Icon, IconButton, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import Space from "../../models/Space";
import { Delete, Edit } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";

const SpaceTable = ({ spaces, on_edit, on_delete }: {
  spaces: Space[],
  on_edit: (id: number) => void,
  on_delete: (id: number) => void
}) => {
  const navigate = useNavigate()

  return <TableContainer> <Table>
    <TableHead>
      <TableRow>
        <TableCell> Name </TableCell>
        <TableCell align="right"> Spark Count </TableCell>
        <TableCell>
          <IconButton disabled> <Icon /> </IconButton>
          <IconButton disabled> <Icon /> </IconButton>
        </TableCell>
      </TableRow>
    </TableHead>

    <TableBody>
      {spaces.sort((a, b) => a.id - b.id).map(space =>
        <TableRow key={space.id} hover onClick={() => navigate(`/spaces/${space.name}`)}>
          <TableCell> {space.name} </TableCell>
          <TableCell align="right"> {space.sparks.length} </TableCell>
          <TableCell align="right">
            <IconButton onClick={() => on_edit(space.id)}> <Edit /> </IconButton>
            <IconButton onClick={() => on_delete(space.id)}> <Delete /> </IconButton>
          </TableCell>
        </TableRow>)}
    </TableBody>
  </Table> </TableContainer >;
}

export default SpaceTable
