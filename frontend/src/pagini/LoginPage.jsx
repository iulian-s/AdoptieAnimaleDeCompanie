import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import "../css/index.css";
import { jwtDecode } from "jwt-decode";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [parola, setParola] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decoded = jwtDecode(token);
                if (decoded.exp * 1000 < Date.now()) {
                    localStorage.removeItem("token");
                    return;
                }
                if (decoded.role === "ROLE_ADMIN") {
                    navigate("/dashboard");
                }
            } catch (err) {
                localStorage.removeItem("token");
            }
        }
    }, [navigate]);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post("/auth/login", { username, parola });
            const token = res.data.token;
            const decoded = jwtDecode(token);

            if (decoded.role === "ROLE_ADMIN") {
                localStorage.setItem("token", token);
                navigate("/dashboard");
            } else {
                setError("Nu te poti conecta cu un cont de utilizator!");
            }
        } catch (err) {
            setError("Username/parola incorecte!");
        }
    };

    return (
        <div className="page">
            <h2>Intra in cont pentru a continua</h2>
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Parola"
                    value={parola}
                    onChange={(e) => setParola(e.target.value)}
                />
                <div style={{ display: "flex", gap: "10px" }}>
                    <button type="submit">Autentificare</button>
                    {/*<button type="button" onClick={() => navigate("/register")}>*/}
                    {/*    Register*/}
                    {/*</button>*/}
                </div>
            </form>
            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );
}
