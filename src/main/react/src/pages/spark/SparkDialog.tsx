import { Autocomplete, Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, Grid, TextField } from "@mui/material"
import SparkEdit from "../../components/spark/SparkEdit"
import { useState } from "react"
import { SparkRquestDto } from "../../api/SparksApi"
import Spark from "../../models/Spark"
import { useDI } from "../../DI"
import SpacesApi from "../../api/SpacesApi"

const SpaceSelect = ({ space_id, on_space_id_change }: {
  space_id: number | undefined,
  on_space_id_change: (id: number | undefined) => void
}) => {
  const spaces_api = useDI()["spaces_api"] as SpacesApi

  const [is_autocomplete_open, set_is_autocomplete_open] = useState(false)
  const [autocomplete_options, set_autocomplete_options] = useState([] as { id: number, name: string }[])
  const [is_autocomplete_loading, set_is_autocomplete_loading] = useState(false)

  const autocomplete_open = async () => {
    set_is_autocomplete_open(true)
    set_is_autocomplete_loading(true)
    const spaces = await spaces_api.search()
    set_is_autocomplete_loading(false)
    set_autocomplete_options(spaces)
  };
  const autocomplete_close = () => set_is_autocomplete_open(false)

  return <Autocomplete open={is_autocomplete_open}
    onOpen={autocomplete_open} onClose={autocomplete_close}
    options={autocomplete_options} getOptionLabel={option => option.name}
    value={autocomplete_options.find(el => el.id == space_id) ?? null}
    onChange={(_, new_value) => new_value && on_space_id_change(new_value.id)}
    renderInput={params =>
      <TextField {...params} label="Space" slotProps={{
        input: {
          ...params.InputProps,
          endAdornment: <>
            {is_autocomplete_loading && <CircularProgress color="inherit" size="1rem" />}
            {params.InputProps.endAdornment}
          </>
        }
      }} />
    } />
}

const SparkDialog = ({ is_open, on_submit, on_cancel, edited }: {
  is_open: boolean,
  on_submit: (value: SparkRquestDto, space_id?: number) => Promise<void>,
  on_cancel: () => void,
  edited?: Spark
}) => {
  const [name, set_name] = useState(edited?.name ?? "")
  const [spectre_names, set_spectre_names] = useState(edited?.spectre_names ?? [] as string[])
  const [space_id, set_space_id] = useState(edited?.space.id)

  return <Dialog disableRestoreFocus open={is_open} onClose={on_cancel} onSubmit={e => {
    e.preventDefault();

    on_submit({ name, spectre_names }, edited ? undefined : space_id)
      .then(() => {
        if (!edited) {
          set_name("")
          set_spectre_names([])
          set_space_id(undefined)
        }
      })
  }}
    slotProps={{ paper: { component: "form" } }}>
    <DialogTitle> {edited ? "Edit Spark" : "Create Spark"} </DialogTitle>

    <DialogContent sx={{ overflow: "unset" }} > <Grid container direction="column" spacing="1.2rem">
      <SparkEdit name={name} on_change_name={set_name}
        spectre_names={spectre_names}
        on_add_spectre={name => set_spectre_names(spectre_names => spectre_names.concat(name))}
        on_remove_spectre={name => set_spectre_names(spectre_names => spectre_names.filter(el => el != name))} />
      {!edited && <SpaceSelect space_id={space_id} on_space_id_change={set_space_id} />}
    </Grid> </DialogContent>

    <DialogActions>
      <Button type="submit"> Ok </Button>
      <Button onClick={on_cancel}> Cancel </Button>
    </DialogActions>

  </Dialog>
}

export default SparkDialog
