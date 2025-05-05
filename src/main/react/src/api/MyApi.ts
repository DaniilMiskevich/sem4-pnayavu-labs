export class ApiError extends Error {
    constructor(error: Error) {
        super(error.message)
        this.name = "API Error"
        Object.setPrototypeOf(this, ApiError.prototype);
    }
}

async function my_fetch(input: RequestInfo | URL, init?: RequestInit) {
    try {
        const response = await fetch(input, init)
        if (!response.ok) throw new Error(`API error [${response.status}]`)
        return response
    } catch (e) {
        if (e instanceof ApiError) throw e
        if (e instanceof Error) throw new ApiError(e)
        else throw e
    }
}

export async function my_get<T extends Object>(input: RequestInfo | URL) {
    return await (await my_fetch(input, { method: "GET" })).json() as T
}
export async function my_post<U extends Object, V extends Object>(input: RequestInfo | URL, val: V) {
    return await (await my_fetch(input, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(val)
    })).json() as U
}
export async function my_patch<U extends Object, V extends Object>(input: RequestInfo | URL, val: V) {
    return await (await my_fetch(input, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(val)
    })).json() as U
}
export async function my_delete(input: RequestInfo | URL) {
    await my_fetch(input, { method: "DELETE" })
}
