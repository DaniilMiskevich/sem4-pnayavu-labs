import { useParams } from "react-router-dom"
import { useDI } from "../DI"
import SparksApi from "../api/SparksApi";
import { Suspense, use } from "react";
import Spark from "../models/Spark";
import SparkComponent from "../components/SparkComponent";

const AsyncSpark = ({ promise }: { promise: Promise<Spark> }) => {
    const spark = use(promise)
    return <SparkComponent
        id={spark.id}
        name={spark.name}
        space_id={spark.space_id}
        spectre_names={spark.spectre_names} />;
}

const SparkPage = () => {
    const { id: id_str } = useParams()
    if (!id_str) return <h1>No Spark ID provided!</h1>
    const id = +id_str
    if (isNaN(id)) return <h1>Invalid Spark ID provided!</h1>

    const sparks_api = useDI()["sparks_api"] as SparksApi;
    const spark = sparks_api.get(id)

    return <Suspense fallback={<h1>Loading...</h1>}>
        <AsyncSpark promise={spark} />
    </Suspense>
}

export default SparkPage
