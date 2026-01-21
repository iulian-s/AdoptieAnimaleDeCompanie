
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import './App.css'
import LoginPage from "./pagini/LoginPage.jsx";
import RegisterPage from "./pagini/RegisterPage.jsx";
import Dashboard from "./pagini/Dashboard.jsx"
import ProtectedRoute from "./ProtectedRoute";
import DetaliiAnunt from "./pagini/DetaliiAnunt.jsx";
import DetaliiUtilizator from "./pagini/DetaliiUtilizator.jsx";

function App() {
    const token = localStorage.getItem("token");

  return (
      <BrowserRouter>
          <Routes>
              <Route path="/login" element={<LoginPage />} />
              {/*<Route path="/register" element={<RegisterPage />} />*/}
              <Route
                  path="/dashboard"
                  element={
                      <ProtectedRoute>
                          <Dashboard />
                      </ProtectedRoute>
                  }
              />
              <Route
                  path="/anunt/:id"
                  element={
                      <ProtectedRoute>
                          <DetaliiAnunt />
                      </ProtectedRoute>
                  }
              />

              <Route
                  path="/utilizator/:id"
                  element={
                      <ProtectedRoute>
                          <DetaliiUtilizator />
                      </ProtectedRoute>
                  }
              />


              <Route path="*" element={
                  <ProtectedRoute>
                  <Navigate to="/login" />
                  </ProtectedRoute>
              } />
          </Routes>
      </BrowserRouter>
  )
}

export default App
