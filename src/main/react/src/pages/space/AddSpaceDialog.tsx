import { Dialog, DialogActions, DialogContent, DialogTitle, IconButton } from "@mui/material"
import { useState } from "react"
import { Close, Done } from "@mui/icons-material"
import SpaceEdit from "../../components/space/SpaceEdit"
import { SpaceRquestDto } from "../../api/SpacesApi"

const AddSpaceDialog = ({ is_open, on_submit, on_cancel }: {
  is_open: boolean,
  on_submit: (value: SpaceRquestDto) => void,
  on_cancel: () => void
}) => {
  const [name, set_name] = useState("")

  return <Dialog disableRestoreFocus open={is_open} onClose={on_cancel} onSubmit={e => {
    e.preventDefault();

    on_submit({ name })

    set_name("")
  }}
    slotProps={{ paper: { component: "form", sx: { borderRadius: "1.2rem" } } }}>
    <DialogTitle> Create Space </DialogTitle>

    <DialogContent> <SpaceEdit name={name} on_change_name={set_name} /> </DialogContent>

    <DialogActions>
      <IconButton type="submit"> <Done /> </IconButton>
      <IconButton onClick={on_cancel}> <Close /> </IconButton>
    </DialogActions>

  </Dialog>
}

export default AddSpaceDialog
