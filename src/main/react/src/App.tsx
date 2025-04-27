import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import SpacePage from './pages/SpacePage';
import SparkPage from './pages/SparkPage';
import HomePage from './pages/HomePage';
import SpectrePage from './pages/SpectrePage';
import SpacesApi from './api/SpacesApi';
import MySpacesApi from './api/MySpacesApi';
import { DIProvider } from './DI';
import SparksApi from './api/SparksApi';
import MySparksApi from './api/MySparksApi';
import AllSparksPage from './pages/AllSparksPage';
import { ThemeProvider } from '@emotion/react';
import { Box, createTheme } from '@mui/material';

const spaces_api: SpacesApi = new MySpacesApi("http://localhost:8080/api/spaces")
const sparks_api: SparksApi = new MySparksApi("http://localhost:8080/api/sparks")

const router = createBrowserRouter([
    { path: "/", element: <HomePage /> },
    { path: "/spaces/:id_or_name", element: <SpacePage /> },
    { path: "/sparks", element: <AllSparksPage /> },
    { path: "/sparks/:id", element: <SparkPage /> },
    { path: "/spectres/:name", element: <SpectrePage /> },
]);

const theme = createTheme({
    palette: {
        background: {
            paper: "#EEEEFF"
        }
    }
})

const App = () => <DIProvider deps={{
    "spaces_api": spaces_api,
    "sparks_api": sparks_api
}}>
    <Box display="flex" justifyContent="center" alignItems="center" >
        <ThemeProvider theme={theme} >
            <RouterProvider router={router} />
        </ThemeProvider>
    </Box>
</DIProvider>

export default App
