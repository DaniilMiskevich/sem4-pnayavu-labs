import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import SpacesApi from './api/SpacesApi';
import MySpacesApi from './api/MySpacesApi';
import { DIProvider } from './DI';
import SparksApi from './api/SparksApi';
import MySparksApi from './api/MySparksApi';
import AllSparksPage from './pages/spark/AllSparksPage';
import { Box } from '@mui/material';
import MyAppBar from './components/MyAppBar';
import AllSpacesPage from './pages/space/AllSpacesPage';

// const spaces_api: SpacesApi = new MySpacesApi("http://localhost:8080/api/spaces")
// const sparks_api: SparksApi = new MySparksApi("http://localhost:8080/api/sparks")
const spaces_api: SpacesApi = new MySpacesApi("https://labs-r37s.onrender.com/api/spaces")
const sparks_api: SparksApi = new MySparksApi("https://labs-r37s.onrender.com/api/sparks")

const router = createBrowserRouter([
  { path: "/", element: <AllSparksPage key="/" /> },
  { path: "/spaces", element: <AllSpacesPage key="/spaces" /> },
  { path: "/spaces/:space_id", element: <AllSparksPage key="/spaces/:space_id" /> },
  { path: "/spectres/:spectre_name", element: <AllSparksPage key="/spectres/:spectre_name" /> },
]);


const App = () => <DIProvider deps={{
  "spaces_api": spaces_api,
  "sparks_api": sparks_api
}}>
  <Box maxWidth="md" justifySelf="center" justifyContent="center" alignItems="center" width="stretch">
    <MyAppBar on_navigate={uri => router.navigate(uri)} />
    <RouterProvider router={router} />
  </Box>
</DIProvider>

export default App
