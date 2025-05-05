import Spark from "../models/Spark";
import { my_delete, my_get, my_patch, my_post } from "./MyApi";
import SparksApi, { SparkRquestDto } from "./SparksApi";

class MySparksApi implements SparksApi {
    constructor(base_url: string) { this.base_uri = base_url }

    async create(space_id: number, spark: SparkRquestDto): Promise<Spark> {
        return my_post(`${this.base_uri}?space_id=${space_id}`, spark)
    }
    async get(id: number): Promise<Spark> {
        return my_get<Spark>(`${this.base_uri}/${id}`)
    }
    async search(name?: string, spectres?: string): Promise<Spark[]> {
        const params = new URLSearchParams({
            ...name && { "name": name },
            ...spectres && { "spectres": spectres },
        });
        return my_get(`${this.base_uri}?${params}`)
    }
    async update(id: number, spark: SparkRquestDto): Promise<Spark> {
        return my_patch(`${this.base_uri}/${id}`, spark)
    }
    async delete(id: number): Promise<void> {
        return my_delete(`${this.base_uri}/${id}`)
    }

    private readonly base_uri: string
}

export default MySparksApi

