import { Box, Chip } from "@mui/material"
import { useNavigate } from "react-router-dom"

const SpectresView = ({ names }: { names?: string[] }) => {
    const navigate = useNavigate()

    return <Box>
        {names
            ? names.map(name =>
                <Chip sx={{ mx: "2px" }} key={name} label={name}
                    onClick={e => {
                        e.stopPropagation()
                        navigate(`/spectres/${name}`)
                    }} />)
            : [0, 1, 2].map(i =>
                <Chip sx={{ mx: "2px", color: "transparent" }} key={i} label="text" />)}
    </Box>
}


export default SpectresView
