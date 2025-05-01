import { useDI } from "../DI"
import SparksApi, { SparkRquestDto } from "../api/SparksApi";
import { Suspense, use, useState } from "react";
import SparkView from "../components/spark/SparkView";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, Button, CircularProgress, Grid, Typography } from "@mui/material";
import Spark from "../models/Spark";
import { Add } from "@mui/icons-material";
import AddSparkDialog from "./AddSparkDialog";

const AsyncAllSparks = ({ sparks_promise }: { sparks_promise: Promise<Spark[]> }) => {
  const sparks_api = useDI()["sparks_api"] as SparksApi;
  const [sparks, set_sparks] = useState(use(sparks_promise))
  const [is_dialog_open, set_is_dialog_open] = useState(false)

  const create_spark = (val: SparkRquestDto) => sparks_api.create(2, val)
    .then(new_spark => set_sparks(sparks => sparks.concat(new_spark)))
    .then(() => set_is_dialog_open(false))
    .catch(() => { })

  const delete_spark = (id: number) => sparks_api.delete(id)
    .then(() => set_sparks(sparks => sparks.filter(el => el.id != id)))
    .catch(() => { })

  return <Grid container direction="column-reverse" spacing="0.6rem">

    {sparks.length <= 0
      ? <Typography color="textDisabled" textAlign="center"> There are no sparks published yet. </Typography>
      : sparks.map(spark =>
        <SparkView key={spark.id} spark={spark}
          on_edit={alert}
          on_delete={() => delete_spark(spark.id)} />)}

    <Button variant="contained" sx={{ borderRadius: "1.2rem", alignSelf: "center" }} startIcon={<Add />}
      onClick={() => set_is_dialog_open(true)}> Create </Button>
    <AddSparkDialog is_open={is_dialog_open} on_submit={create_spark} on_cancel={() => set_is_dialog_open(false)} />

  </Grid>
}

const AllSparksPage = () => {
  const sparks_api = useDI()["sparks_api"] as SparksApi;
  const sparks_promise = sparks_api.search()

  return <>
    <Suspense fallback={
      <Box display="flex" justifyContent="center"> <CircularProgress /> </Box>
    }>
      <ErrorBoundary fallbackRender={({ error }) =>
        <Box display="flex" justifyContent="center"> <Alert severity="error"> {error.toString()} </Alert> </Box>
      }>
        <AsyncAllSparks sparks_promise={sparks_promise} />
      </ErrorBoundary>
    </Suspense>
  </>
}

export default AllSparksPage
