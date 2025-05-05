import Space from "../models/Space";
import { my_delete, my_get, my_patch, my_post } from "./MyApi";
import SpacesApi, { SpaceRquestDto } from "./SpacesApi";

class MySpacesApi implements SpacesApi {
    constructor(base_url: string) { this.base_uri = base_url }

    async create(space: SpaceRquestDto): Promise<Space> {
        return my_post(this.base_uri, space)
    }
    async get(idOrName: string): Promise<Space> {
        return my_get(`${this.base_uri}/${idOrName}`)
    }
    async search(name?: string): Promise<Space[]> {
        const params = new URLSearchParams({
            ...name && { "name": name },
        });
        return my_get(`${this.base_uri}?${params}`)
    }
    async update(id: number, space: SpaceRquestDto): Promise<Space> {
        return my_patch(`${this.base_uri}/${id}`, space)
    }
    async delete(id: number): Promise<void> {
        return my_delete(`${this.base_uri}/${id}`)
    }

    private readonly base_uri: string
}

export default MySpacesApi
