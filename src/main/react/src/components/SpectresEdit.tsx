import { Add } from "@mui/icons-material";
import { Chip, Grid, IconButton, InputAdornment, TextField } from "@mui/material"
import { useState } from "react";
import { useNavigate } from "react-router-dom"

const SpectresEdit = ({ names, on_add, on_remove }: {
  names: string[],
  on_add: (name: string) => void,
  on_remove: (name: string) => void
}) => {
  const navigate = useNavigate();

  const [new_name, set_new_name] = useState("")

  return <Grid container spacing="0.3rem">

    {names.map(name => <Chip key={name} label={name}
      onClick={() => navigate(`/spectres/${name}`)} onDelete={() => on_remove(new_name)} />)}

    &nbsp;

    <TextField placeholder="Add" variant="standard" sx={{ width: "5rem" }}
      value={new_name} onChange={e => set_new_name(e.target.value)}
      slotProps={{
        input: {
          endAdornment:
            <InputAdornment position="end">
              <IconButton size="small" disabled={!new_name}
                onClick={() => {
                  const n = new_name.toLowerCase()
                  if (!names.find(el => el == n)) on_add(n)
                  set_new_name("")
                }}>
                <Add />
              </IconButton>
            </InputAdornment>
        }
      }} />

  </Grid>
}


export default SpectresEdit
