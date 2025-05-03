import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from "@mui/material"
import { useState } from "react"
import SpaceEdit from "../../components/space/SpaceEdit"
import { SpaceRquestDto } from "../../api/SpacesApi"
import Space from "../../models/Space"

const SpaceDialog = ({ is_open, on_submit, on_cancel, edited }: {
  is_open: boolean,
  on_submit: (value: SpaceRquestDto) => Promise<void>,
  on_cancel: () => void
  edited?: Space
}) => {
  const [name, set_name] = useState(edited?.name ?? "")

  return <Dialog disableRestoreFocus open={is_open} onClose={on_cancel} onSubmit={e => {
    e.preventDefault();

    on_submit({ name })
      .then(() => set_name(""))
  }}
    slotProps={{ paper: { component: "form" } }}>
    <DialogTitle> {edited ? "Edit Space" : "Create Space"} </DialogTitle>

    <DialogContent> <SpaceEdit name={name} on_name_change={set_name} /> </DialogContent>

    <DialogActions>
      <Button type="submit"> Ok </Button>
      <Button onClick={on_cancel}> Cancel </Button>
    </DialogActions>

  </Dialog>
}

export default SpaceDialog
