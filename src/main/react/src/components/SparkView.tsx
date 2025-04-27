import SpectresView from "./SpectresView"
import Spark from "../models/Spark"
import { Card, CardActionArea, CardActions, CardContent, Typography } from "@mui/material"
import { useNavigate } from "react-router-dom"

const SparkView = ({ spark }: { spark?: Spark }) => {
    const navigate = useNavigate();

    return <Card
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

export default SparkView 
