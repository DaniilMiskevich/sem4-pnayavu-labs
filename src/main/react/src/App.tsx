import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import SpacePage from './pages/SpacePage';
import SparkPage from './pages/SparkPage';
import HomePage from './pages/HomePage';
import SpectrePage from './pages/SpectrePage';
import SpacesApi from './api/SpacesApi';
import MySpacesApi from './api/MySpacesApi';
import { DIProvider } from './DI';

const spaces_api: SpacesApi = new MySpacesApi("http://localhost:8080/api/spaces")
const sparks_api: SpacesApi = new MySpacesApi("http://localhost:8080/api/sparks")

const router = createBrowserRouter([
    { path: "/", element: <HomePage /> },
    { path: "/space/:id_or_name", element: <SpacePage /> },
    { path: "/spark/:id", element: <SparkPage /> },
    { path: "/spectre/:name", element: <SpectrePage /> },
]);

const App = () => <DIProvider deps={{
    "spaces_api": spaces_api,
    "sparks_api": sparks_api
}}>
    <RouterProvider router={router} />
</DIProvider>

export default App
