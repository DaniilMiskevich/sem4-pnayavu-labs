import Space from "../models/Space"

type SpaceRquestDto = { name: string }
interface SpacesApi {
    create(space: SpaceRquestDto): Promise<Space>
    get(idOrName: string): Promise<Space>
    search(name?: string): Promise<Space[]>
    update(id: number, space: SpaceRquestDto): Promise<Space>
    delete(id: number): Promise<void>
}

export default SpacesApi
export type { SpaceRquestDto } 
