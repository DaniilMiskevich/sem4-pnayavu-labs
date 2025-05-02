import { Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from "@mui/material"
import SparkEdit from "../../components/spark/SparkEdit"
import { useState } from "react"
import { SparkRquestDto } from "../../api/SparksApi"
import { Close, Done } from "@mui/icons-material"
import Spark from "../../models/Spark"

const SparkDialog = ({ is_open, on_submit, on_cancel, edited }: {
  is_open: boolean,
  on_submit: (value: SparkRquestDto, space_id?: number) => Promise<void>,
  on_cancel: () => void,
  edited?: Spark
}) => {
  const [name, set_name] = useState(edited?.name ?? "")
  const [spectre_names, set_spectre_names] = useState(edited?.spectre_names ?? [] as string[])
  const [space_id, set_space_id] = useState(edited?.space_id ?? 32 as number | undefined)

  return <Dialog disableRestoreFocus open={is_open} onClose={on_cancel} onSubmit={e => {
    e.preventDefault();

    on_submit({ name, spectre_names }, space_id)
      .then(() => { set_name(""); set_spectre_names([]); })
  }}
    slotProps={{ paper: { component: "form" } }}>
    <DialogTitle> {edited ? "Edit Spark" : "Create Spark"} </DialogTitle>

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

export default SparkDialog
