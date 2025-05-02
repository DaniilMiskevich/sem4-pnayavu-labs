import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import SpacePage from './pages/space/SpacePage';
import SpectrePage from './pages/SpectrePage';
import SpacesApi from './api/SpacesApi';
import MySpacesApi from './api/MySpacesApi';
import { DIProvider } from './DI';
import SparksApi from './api/SparksApi';
import MySparksApi from './api/MySparksApi';
import AllSparksPage from './pages/spark/AllSparksPage';
import { ThemeProvider } from '@emotion/react';
import { Box, createTheme, } from '@mui/material';
import MyAppBar from './components/MyAppBar';
import AllSpacesPage from './pages/space/AllSpacesPage';

const spaces_api: SpacesApi = new MySpacesApi("http://localhost:8080/api/spaces")
const sparks_api: SparksApi = new MySparksApi("http://localhost:8080/api/sparks")

const router = createBrowserRouter([
  { path: "/", element: <AllSparksPage /> },
  { path: "/spaces", element: <AllSpacesPage /> },
  { path: "/spaces/:id_or_name", element: <SpacePage /> },
  { path: "/sparks", element: <AllSparksPage /> },
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
  <ThemeProvider theme={theme}>
    <MyAppBar onClick={() => router.navigate("/")} />
    <Box maxWidth="md" justifySelf="center" justifyContent="center" alignItems="center" width="stretch">
      <RouterProvider router={router} />
    </Box>
  </ThemeProvider>
</DIProvider>

export default App
