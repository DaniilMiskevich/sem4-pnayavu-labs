import { useDI } from "../DI"
import SparksApi from "../api/SparksApi";
import { Suspense, use } from "react";
import Spark from "../models/Spark";
import SparkView from "../components/spark/SparkView";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, CircularProgress, Grid, Typography } from "@mui/material";

const AsyncAllSpaces = ({ promise }: { promise: Promise<Spark[]> }) => {
  const sparks = use(promise)

  if (sparks.length <= 0) return <p>No sparks published yet!</p>
  return <Grid>{
    sparks.map(spark => <SparkView key={spark?.id} spark={spark} />)
  }</Grid>
}

const AllSpacesPage = () => {
  const sparks_api = useDI()["sparks_api"] as SparksApi;
  const sparks = sparks_api.search()

  return <>
    <Typography variant="h2" textAlign="center" gutterBottom>All Sparks ✨</Typography>
    <Suspense fallback={
      <Box display="flex" justifyContent="center"> <CircularProgress /> </Box>
    }>
      <ErrorBoundary fallbackRender={({ error }) =>
        <Alert severity="error">{error.toString()}</Alert>
      }>
        <AsyncAllSpaces promise={sparks} />
      </ErrorBoundary>
    </Suspense>
  </>
}

export default AllSpacesPage
