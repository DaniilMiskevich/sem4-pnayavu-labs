import { useParams } from "react-router-dom"

const SpectrePage = () => {
  const { name } = useParams()

  return <>
    <h1>Sparks with spectre {name}</h1>
  </>
}

export default SpectrePage
