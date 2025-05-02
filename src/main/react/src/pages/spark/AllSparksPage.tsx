import { useDI } from "../../DI"
import SparksApi, { SparkRquestDto } from "../../api/SparksApi";
import { Suspense, use, useState } from "react";
import SparkTable from "../../components/spark/SparkTable";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, Button, CircularProgress, Grid } from "@mui/material";
import Spark from "../../models/Spark";
import { Add } from "@mui/icons-material";
import SparkDialog from "./SparkDialog";

const AsyncAllSparks = ({ sparks_promise }: { sparks_promise: Promise<Spark[]> }) => {
  const sparks_api = useDI()["sparks_api"] as SparksApi;
  const [sparks, set_sparks] = useState(use(sparks_promise))
  const [dialog_state, set_dialog_state] = useState({ is_open: false, edited: undefined as Spark | undefined })

  const create_spark = (val: SparkRquestDto, space_id?: number) => sparks_api.create(space_id ?? 0, val)
    .then(new_spark => set_sparks(sparks => sparks.concat(new_spark)))
    .then(() => set_dialog_state({ is_open: false, edited: undefined }))

  const delete_spark = (id: number) => sparks_api.delete(id)
    .then(() => set_sparks(sparks => sparks.filter(el => el.id != id)))

  return <Grid container direction="column" spacing="0.6rem" alignItems="center">
    <Button variant="contained" startIcon={<Add />}
      onClick={() => set_dialog_state({ is_open: true, edited: undefined })}> Create </Button>
    <SparkDialog is_open={dialog_state.is_open} edited={dialog_state.edited}
      on_submit={create_spark}
      on_cancel={() => set_dialog_state({ is_open: false, edited: undefined })} />

    <SparkTable sparks={sparks}
      on_edit={id => set_dialog_state({ is_open: true, edited: sparks.find(el => el.id == id) })}
      on_delete={delete_spark} />
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
