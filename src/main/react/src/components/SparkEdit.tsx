import SpectresView from "./SpectresView"
import Spark from "../models/Spark"
import { Card, CardActionArea, CardActions, CardContent, Typography } from "@mui/material"
import { useNavigate } from "react-router-dom"

const SparkEdit = ({ spark }: { spark?: Spark }) => {
    const navigate = useNavigate();

    return <Card component="form"
        sx={{ margin: "1rem" }}>
        <CardActionArea sx={{ padding: "0.5rem" }}
            onClick={!spark ? undefined : e => {
                e.stopPropagation()
                navigate(`/sparks/${spark.id}`)
            }}>
            <CardContent>
                <Typography variant="h5">{spark ? spark.name : "..."}</Typography>
            </CardContent>
            <CardActions>
                <SpectresView names={spark?.spectre_names} />
            </CardActions>
        </CardActionArea>
    </Card >
}

export default SparkEdit 
