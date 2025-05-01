import { useParams } from "react-router-dom"

const SpacePage = () => {
  const { id_or_name } = useParams()

  return <>
    <h1>{id_or_name} info</h1>
    <h1>Sparks in {id_or_name}</h1>
  </>
}

export default SpacePage
