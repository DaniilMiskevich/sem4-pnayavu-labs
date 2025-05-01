import { Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from "@mui/material"
import SparkEdit from "../components/spark/SparkEdit"
import { useState } from "react"
import { SparkRquestDto } from "../api/SparksApi"
import { Close, Done } from "@mui/icons-material"

const AddSparkDialog = ({ is_open, on_submit, on_cancel }: {
  is_open: boolean,
  on_submit: (value: SparkRquestDto) => void,
  on_cancel: () => void
}) => {
  const [name, set_name] = useState("")
  const [spectre_names, set_spectre_names] = useState([] as string[])

  return <Dialog disableRestoreFocus open={is_open} onClose={on_cancel} onSubmit={e => {
    e.preventDefault();

    on_submit({ name, spectre_names })

    set_name("")
    set_spectre_names([])
  }}
    slotProps={{ paper: { component: "form", sx: { borderRadius: "1.2rem" } } }}>
    <DialogTitle> Create Spark </DialogTitle>

    <DialogContent>
      <SparkEdit name={name} on_change_name={set_name}
        spectre_names={spectre_names}
        on_add_spectre={name => set_spectre_names(spectre_names => spectre_names.concat(name))}
        on_remove_spectre={name => set_spectre_names(spectre_names => spectre_names.filter(el => el != name))} />
    </DialogContent>

    <DialogActions>
      <IconButton type="submit"> <Done /> </IconButton>
      <IconButton onClick={on_cancel}> <Close /> </IconButton>
    </DialogActions>

  </Dialog>
}

export default AddSparkDialog
