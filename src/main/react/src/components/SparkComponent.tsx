import Spectres from "./SpectresComponent"
import Spark from "../models/Spark"

const SparkComponent = ({ name, spectre_names }: Spark) => <div className="card container-sm">
    <div className="card-body">
        <h5 className="card-title">{name}</h5>
        <Spectres names={spectre_names} />
    </div>
</div>

export default SparkComponent
