import { Navigate, useParams } from "react-router-dom"
import { useDI } from "../DI"
import SparksApi from "../api/SparksApi";
import { Suspense, use } from "react";
import Spark from "../models/Spark";
import SparkView from "../components/SparkView";
import SparkEdit from "../components/SparkEdit";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, CircularProgress, Typography } from "@mui/material";

const AsyncSpark = ({ promise }: { promise: Promise<Spark> }) => {
    const spark = use(promise)
    return <SparkEdit spark={spark} onDelete={() => { }} />
}

const SparkPage = () => {
    const { id: id_str } = useParams()
    const id = +id_str!
    if (isNaN(id)) return <Navigate to="/sparks" />

    const sparks_api = useDI()["sparks_api"] as SparksApi;
    const spark = sparks_api.get(id)

    return <Box>
        <Typography variant="h2" textAlign="center" gutterBottom>Spark Details</Typography>
        <Suspense fallback={
            <Box display="flex" justifyContent="center" alignItems="center"> <CircularProgress /> </Box>
        }>
            <ErrorBoundary fallbackRender={({ error }) =>
                <Alert severity="error">{error.toString()}</Alert>
            }>
                <AsyncSpark promise={spark} />
            </ErrorBoundary>
        </Suspense>
    </Box>
}

export default SparkPage
