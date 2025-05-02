import { Grid, TextField } from "@mui/material"

const SpaceEdit = ({ name, on_change_name }: { name: string, on_change_name: (val: string) => void }) => <Grid container direction="column">
  <TextField required fullWidth placeholder="Space Name" slotProps={{ input: { sx: { borderRadius: "1.2rem" } } }}
    value={name} onChange={e => on_change_name(e.target.value)} />
</Grid>

export default SpaceEdit 
