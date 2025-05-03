import { Add } from "@mui/icons-material";
import { Button, Chip, Grid, InputAdornment, TextField } from "@mui/material"
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const SpectresEdit = ({ names, on_add, on_remove }: {
  names: string[],
  on_add: (name: string) => void,
  on_remove: (name: string) => void
}) => {
  const navigate = useNavigate()
  const [new_name, set_new_name] = useState("")

  const add_name = () => {
    if (!names.find(el => el == new_name)) on_add(new_name)
    set_new_name("")
  }

  return <Grid container direction="row" maxWidth="300px" spacing="0.3rem">
    <TextField placeholder="Spectre" variant="standard" sx={{ minWidth: "calc(7ch + 24pt)", width: `calc(${new_name.length}ch + 24pt)` }}
      value={new_name} onChange={e => set_new_name(e.target.value.toLowerCase())} onKeyDown={e => {
        if (new_name && e.key == "Enter") {
          e.preventDefault()
          add_name()
        }
      }}
      slotProps={{
        input: {
          endAdornment:
            <InputAdornment position="end">
              <Button disabled={!new_name} sx={{ minWidth: "auto", padding: "0", borderRadius: "calc(infinity * 1px)" }} onClick={add_name}>
                <Add />
              </Button>
            </InputAdornment>
        }
      }} />

    {names.sort((a, b) => a.length - b.length).map(name => <Chip key={name} label={name}
      onClick={() => navigate(`/spectres/${name}`)} onDelete={() => on_remove(name)} />)}

  </Grid>
}


export default SpectresEdit
