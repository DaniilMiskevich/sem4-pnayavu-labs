import { Grid, TextField } from "@mui/material"

const SpaceEdit = ({ name, on_name_change }: { name: string, on_name_change: (val: string) => void }) => <Grid container direction="column">
  <TextField required fullWidth name="name" placeholder="Space Name"
    value={name} onChange={e => on_name_change(e.target.value)} />
</Grid>

export default SpaceEdit 
