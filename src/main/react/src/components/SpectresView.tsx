import { Chip, Grid } from "@mui/material"
import { useNavigate } from "react-router-dom"

const SpectresView = ({ names }: { names?: string[] }) => {
  const navigate = useNavigate()

  return <Grid container spacing="0.3rem">
    {names
      ? names.map(name =>
        <Chip key={name} label={name}
          onClick={() => navigate(`/spectres/${name}`)} />)
      : [0, 1, 2].map(i =>
        <Chip sx={{ color: "transparent" }} key={i} label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" />)}
  </Grid>
}


export default SpectresView
