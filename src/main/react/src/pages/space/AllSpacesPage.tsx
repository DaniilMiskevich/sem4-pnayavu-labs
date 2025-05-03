import { useDI } from "../../DI"
import { Suspense, use, useState } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { Alert, Box, Button, CircularProgress, Grid } from "@mui/material";
import { Add } from "@mui/icons-material";
import SpacesApi, { SpaceRquestDto } from "../../api/SpacesApi";
import Space from "../../models/Space";
import SpaceDialog from "./SpaceDialog";
import SpaceTable from "../../components/space/SpaceTable";

const AsyncAllSpaces = ({ spaces_promise }: { spaces_promise: Promise<Space[]> }) => {
  const spaces_api = useDI()["spaces_api"] as SpacesApi;
  const [spaces, set_spaces] = useState(use(spaces_promise))

  const [dialog_state, set_dialog_state] = useState({ is_open: false } as { is_open: boolean, edited?: Space })
  const dialog_close = () => set_dialog_state(state => ({ ...state, is_open: false }))
  const dialog_create = () => set_dialog_state({ edited: undefined, is_open: true })
  const dialog_edit = (edited?: Space) => set_dialog_state({ edited, is_open: true })

  const create_space = (val: SpaceRquestDto) => spaces_api.create(val)
    .then(new_space => set_spaces(spaces => spaces.concat(new_space)))
    .then(dialog_close)

  const update_space = (val: SpaceRquestDto) => spaces_api.update(dialog_state.edited!.id, val)
    .then(updated_space => set_spaces(spaces => spaces.filter(el => el.id != updated_space.id).concat(updated_space)))
    .then(dialog_close)

  const delete_space = (id: number) => spaces_api.delete(id)
    .then(() => set_spaces(spaces => spaces.filter(el => el.id != id)))

  return <Grid container direction="column" alignItems="center">
    <Button variant="contained" startIcon={<Add />} onClick={dialog_create}> Create </Button>
    <SpaceDialog key={dialog_state.edited?.id} {...dialog_state}
      on_submit={dialog_state.edited ? update_space : create_space} on_cancel={dialog_close} />

    <SpaceTable spaces={spaces}
      on_edit={id => dialog_edit(spaces.find(el => el.id == id))} on_delete={delete_space} />
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
