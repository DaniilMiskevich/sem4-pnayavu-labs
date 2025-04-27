import Space from "../models/Space";
import { my_fetch } from "./MyApi";
import SpacesApi, { SpaceRquestDto } from "./SpacesApi";

class MySpacesApi implements SpacesApi {
    constructor(base_url: string) { this.base_uri = base_url }

    async create(space: SpaceRquestDto): Promise<Space> {
        return my_fetch<Space>(this.base_uri, { method: "POST", body: JSON.stringify(space) })
    }
    async get(idOrName: number | string): Promise<Space> {
        return my_fetch<Space>(`${this.base_uri}/${idOrName}`)
    }
    async search(name?: string): Promise<Space[]> {
        const params = new URLSearchParams({
            ...name && { "name": name },
        });
        return my_fetch<Space[]>(`${this.base_uri}?${params}`)
    }
    async update(id: number, space: SpaceRquestDto): Promise<Space> {
        return my_fetch<Space>(`${this.base_uri}/${id}`, { method: "PATCH", body: JSON.stringify(space) })
    }
    async delete(id: number): Promise<void> {
        return my_fetch<void>(`${this.base_uri}/${id}`, { method: "DELETE" })
    }

    private readonly base_uri: string
}

export default MySpacesApi
