import {useEffect, useState} from "react";
import api from "../api";
import "../css/tab1.css";

export default function Utilizatori(){
    const [utilizatori, setUtilizatori] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [sortType, setSortType] = useState("id_desc");


    useEffect(() => {
        api.get("/utilizator")
            .then((res) => setUtilizatori(res.data))
            .catch((err) => console.error(err));
    }, []);

    const utilizatoriSortati = utilizatori
        .filter((a) => {
            // Cautare dupa id, nume sau username
            const term = searchTerm.toLowerCase();
            return a.id.toString().includes(term) ||
                a.nume.toLowerCase().includes(term) ||
                a.username.toLowerCase().includes(term);
        })
        .sort((a, b) => {
            switch (sortType) {
                case "nume_asc":
                    return a.nume.localeCompare(b.nume);
                case "nume_desc":
                    return b.nume.localeCompare(a.nume);
                case "id_desc":
                default:
                    return b.id - a.id;
            }
        });

    const handleRowClick = (id) => {
        window.open(`/utilizator/${id}`, "_blank");
    };

    return (
        <div className="table-container">
            {/* Cautare + Sort */}
            <div style={{ marginBottom: "12px", display: "flex", gap: "12px", flexWrap: "wrap" }}>
                <input
                    type="text"
                    placeholder="Cauta dupa ID, username, nume..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    style={{ padding: "6px", flex: "1 1 200px" }}
                />


                <select value={sortType} onChange={(e) => setSortType(e.target.value)} style={{ padding: "6px" }}>
                    <option value="id_desc">Cei mai recenti</option>
                    <option value="nume_asc">Nume A → Z</option>
                    <option value="nume_desc">Nume Z → A</option>
                </select>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Rol</th>

                </tr>
                </thead>
                <tbody>
                {utilizatoriSortati.map((a)=> (
                    <tr key={a.id} onClick={() => handleRowClick(a.id)} style={{ cursor: "pointer" }}>
                        <td>{a.id}</td>
                        <td>{a.username}</td>
                        <td>{a.rol}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}