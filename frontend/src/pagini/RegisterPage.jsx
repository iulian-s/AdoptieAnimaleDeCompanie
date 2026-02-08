import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import "../css/index.css"

export default function RegisterPage() {
    const [username, setUsername] = useState("");
    const [parola, setParola] = useState("");
    const [email, setEmail] = useState("")
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await api.post("/auth/register", { username, parola, email });
            navigate("/login");
        } catch (err) {
            alert("Registration failed");
        }
    };

    return (
        <div className="page">
            <h2>Register</h2>
            <form onSubmit={handleRegister}>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Parola"
                    value={parola}
                    onChange={(e) => setParola(e.target.value)}
                />
                <div style={{ display: "flex", gap: "10px" }}>
                    <button type="submit">Inregistrare</button>
                    <button type="button" onClick={() => navigate("/")}>
                        Back
                    </button>
                </div>
            </form>
        </div>
    );
}