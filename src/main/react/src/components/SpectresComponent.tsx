import { Link } from "react-router-dom"

const SpectresComponent = ({ names }: { names: string[] }) => <span className="flex-row ">
    {names.map(name =>
        <Link className="col badge rounded-pill text-bg-primary m-1" to={`/spectres/${name}`}>
            {name}
        </Link>
    )}
</span>

export default SpectresComponent
