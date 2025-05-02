import { useDI } from "../../DI"
import { Suspense, use, useState } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, Button, CircularProgress, Grid } from "@mui/material";
import { Add } from "@mui/icons-material";
import SpacesApi, { SpaceRquestDto } from "../../api/SpacesApi";
import Space from "../../models/Space";
import AddSpaceDialog from "./AddSpaceDialog";
import SpaceTable from "../../components/space/SpaceTable";

const AsyncAllSpaces = ({ spaces_promise }: { spaces_promise: Promise<Space[]> }) => {
  const spaces_api = useDI()["spaces_api"] as SpacesApi;
  const [spaces, set_spaces] = useState(use(spaces_promise))
  const [is_dialog_open, set_is_dialog_open] = useState(false)

  const create_space = (val: SpaceRquestDto) => spaces_api.create(val)
    .then(new_space => set_spaces(spaces => spaces.concat(new_space)))
    .then(() => set_is_dialog_open(false))

  const delete_space = (id: number) => spaces_api.delete(id)
    .then(() => set_spaces(spaces => spaces.filter(el => el.id != id)))

  return <Grid container direction="column" spacing="0.6rem" alignItems="center">
    <Button variant="contained" startIcon={<Add />}
      onClick={() => set_is_dialog_open(true)}> Create </Button>
    <AddSpaceDialog is_open={is_dialog_open} on_submit={create_space}
      on_cancel={() => set_is_dialog_open(false)} />

    <SpaceTable spaces={spaces} on_edit={alert} on_delete={delete_space} />
  </Grid>
}

const AllSpacesPage = () => {
  const spaces_api = useDI()["spaces_api"] as SpacesApi;
  const spaces_promise = spaces_api.search()

  return <>
    <Suspense fallback={
      <Box display="flex" justifyContent="center"> <CircularProgress /> </Box>
    }>
      <ErrorBoundary fallbackRender={({ error }) =>
        <Box display="flex" justifyContent="center"> <Alert severity="error"> {error.toString()} </Alert> </Box>
      }>
        <AsyncAllSpaces spaces_promise={spaces_promise} />
      </ErrorBoundary>
    </Suspense>
  </>
}

export default AllSpacesPage
