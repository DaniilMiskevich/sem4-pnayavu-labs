import Spark from "./Spark"

class Space {
    constructor({ id, name, sparks }: { id: number, name: string, sparks: Spark[] }) {
        this.id = id
        this.name = name
        this.sparks = sparks
    }

    id: number
    name: string
    sparks: Spark[]
}

export default Space
