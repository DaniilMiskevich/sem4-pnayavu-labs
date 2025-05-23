import { useDI } from "../../DI"
import SparksApi, { SparkRquestDto } from "../../api/SparksApi";
import { Suspense, use, useState } from "react";
import SparkTable from "../../components/spark/SparkTable";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, Button, CircularProgress, Grid } from "@mui/material";
import Spark from "../../models/Spark";
import { Add } from "@mui/icons-material";
import SparkDialog from "./SparkDialog";
import SpacesApi from "../../api/SpacesApi";
import { useParams } from "react-router-dom";

const AsyncAllSparks = ({ sparks_promise }: { sparks_promise: Promise<Spark[]> }) => {
  const spectre_name = useParams()["spectre_name"]

  const sparks_api = useDI()["sparks_api"] as SparksApi;
  const [sparks, set_sparks] = useState(use(sparks_promise))

  const [dialog_state, set_dialog_state] = useState({ is_open: false } as { is_open: boolean, edited?: Spark })
  const dialog_close = () => set_dialog_state(state => ({ ...state, is_open: false }))
  const dialog_create = () => set_dialog_state({ edited: undefined, is_open: true })
  const dialog_edit = (edited?: Spark) => set_dialog_state({ edited, is_open: true })

  const create_spark = (val: SparkRquestDto, space_id?: number) => sparks_api.create(space_id ?? 0, val)
    .then(new_spark => set_sparks(sparks => sparks.concat(new_spark)))
    .then(dialog_close)

  const update_spark = (val: SparkRquestDto) => sparks_api.update(dialog_state.edited!.id, val)
    .then(updated_spark => set_sparks(sparks => sparks.filter(el => el.id != updated_spark.id).concat(updated_spark)))
    .then(dialog_close)

  const delete_spark = (id: number) => sparks_api.delete(id)
    .then(() => set_sparks(sparks => sparks.filter(el => el.id != id)))

  return <Grid container direction="column" alignItems="center">
    <SparkDialog key={dialog_state.edited?.id} {...dialog_state}
      on_submit={dialog_state.edited ? update_spark : create_spark} on_cancel={dialog_close} />
    {!spectre_name && <Button variant="contained" startIcon={<Add />} onClick={dialog_create}> Create </Button>}
    <SparkTable sparks={sparks}
      on_edit={id => dialog_edit(sparks.find(el => el.id == id))} on_delete={delete_spark} />
  </Grid>
}

const AllSparksPage = () => {
  const space_id = useParams()["space_id"]
  const spectre_name = useParams()["spectre_name"]

  const sparks_api = useDI()["sparks_api"] as SparksApi;
  const spaces_api = useDI()["spaces_api"] as SpacesApi;

  const sparks_promise = space_id
    ? spaces_api.get(space_id).then(space => space.sparks)
    : sparks_api.search(undefined, spectre_name)

  return <>
    <Suspense key={spectre_name} fallback={
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
