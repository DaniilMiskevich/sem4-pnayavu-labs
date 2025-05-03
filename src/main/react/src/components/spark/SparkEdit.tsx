import { Grid, TextField } from "@mui/material"
import SpectresEdit from "../SpectresEdit"

const SparkEdit = ({ name, on_change_name, spectre_names, on_add_spectre, on_remove_spectre }: {
  name: string,
  on_change_name: (val: string) => void,
  spectre_names: string[],
  on_add_spectre: (name: string) => void,
  on_remove_spectre: (name: string) => void
}) => <Grid container direction="column" spacing="1.2rem">
    <TextField fullWidth label="Spark Name"
      value={name} onChange={e => on_change_name(e.target.value)} />
    <SpectresEdit names={spectre_names} on_add={on_add_spectre} on_remove={on_remove_spectre} />
  </Grid>

export default SparkEdit 
