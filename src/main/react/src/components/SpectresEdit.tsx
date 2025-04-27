import { Chip, Grid, TextField } from "@mui/material"
import { useNavigate } from "react-router-dom"

const SpectresEdit = ({ names, on_add, on_remove }: {
    names: string[],
    on_add: (name: string) => void,
    on_remove: (name: string) => void
}) => {
    const navigate = useNavigate();

    return <Grid>
        {names.map(name => <Chip key={name} label={name}
            onClick={() => navigate(`/spectres/${name}`)} onDelete={() => on_remove(name)} />)}
        <TextField placeholder="new spark" onSubmit={() => alert("")} />
    </Grid>
}


export default SpectresEdit
