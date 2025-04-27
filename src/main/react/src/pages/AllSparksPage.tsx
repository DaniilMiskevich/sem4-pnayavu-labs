import { useDI } from "../DI"
import SparksApi from "../api/SparksApi";
import { Suspense, use } from "react";
import Spark from "../models/Spark";
import SparkView from "../components/SparkView";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, CircularProgress, Grid, Typography } from "@mui/material";

const AsyncAllSparks = ({ promise }: { promise: Promise<Spark[]> }) => {
    const sparks = use(promise)

    if (sparks.length <= 0) return <p>No sparks published yet!</p>
    return <Grid>{
        sparks.map(spark => <SparkView key={spark?.id} spark={spark} />)
    }</Grid>
}

const AllSparksPage = () => {
    const sparks_api = useDI()["sparks_api"] as SparksApi;
    const sparks = sparks_api.search()

    return <Box>
        <Typography variant="h2" textAlign="center" gutterBottom>All Sparks ✨</Typography>
        <Suspense fallback={
            <Box sx={{ display: "flex", justifyContent: "center" }}> <CircularProgress /> </Box>
        }>
            <ErrorBoundary fallbackRender={({ error }) =>
                <Alert severity="error">{error.toString()}</Alert>
            }>
                <AsyncAllSparks promise={sparks} />
            </ErrorBoundary>
        </Suspense>
    </Box>
}

export default AllSparksPage
