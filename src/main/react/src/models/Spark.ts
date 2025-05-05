class Spark {
    constructor({ id, name, space, spectre_names }: {
        id: number,
        name: string,
        space: { id: number, name: string },
        spectre_names: string[]
    }) {
        this.id = id
        this.name = name
        this.space = space
        this.spectre_names = spectre_names
    }

    id: number
    name: string
    space: { id: number, name: string }
    spectre_names: string[]
}

export default Spark
