class Spark {
    constructor({ id, name, space_id, spectre_names }: { id: number, name: string, space_id: number, spectre_names: string[] }) {
        this.id = id
        this.name = name
        this.space_id = space_id
        this.spectre_names = spectre_names
    }

    id: number
    name: string
    space_id: number
    spectre_names: string[]
}

export default Spark
