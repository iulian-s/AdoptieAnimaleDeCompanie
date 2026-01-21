import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import "../css/tab1.css";

export default function Anunturi() {
    const [anunturi, setAnunturi] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [statusFilter, setStatusFilter] = useState("TOATE");
    const [sortType, setSortType] = useState("id_desc");

    useEffect(() => {
        let isMounted = true;

        const fetchAnunturi = async () => {
            try {
                const res = await api.get("/anunturi");
                if (isMounted) setAnunturi(res.data);
            } catch (err) {
                console.error(err);
            }
        };

        fetchAnunturi();
        const interval = setInterval(fetchAnunturi, 5000);

        return () => {
            isMounted = false;
            clearInterval(interval);
        };
    }, []);


    // Filtrare + cautare + sortare
    const anunturiFiltrate = anunturi
        .filter((a) => {
            // Cautare dupa id, titlu sau specie
            const term = searchTerm.toLowerCase();
            const matchSearch =
                a.id.toString().includes(term) ||
                a.titlu.toLowerCase().includes(term) ||
                a.specie.toLowerCase().includes(term);

            // Filtrare dupa stare
            const matchStatus = statusFilter === "TOATE" || a.stare === statusFilter;

            return matchSearch && matchStatus;
        })
        .sort((a, b) => {
            switch (sortType) {
                case "titlu_asc":
                    return a.titlu.localeCompare(b.titlu);
                case "titlu_desc":
                    return b.titlu.localeCompare(a.titlu);
                case "id_desc":
                default:
                    return b.id - a.id;
            }
        });

    const handleRowClick = (id) => {
        window.open(`/anunt/${id}`, "_blank");
    };

    const handleStareChange = (id, newStare) => {
        const anuntCurent = anunturi.find(a => a.id === id);
        if (!anuntCurent) return;

        if (!window.confirm("Esti sigur ca modifici starea anuntului?")) return;

        const dto = {
            id: anuntCurent.id,
            titlu: anuntCurent.titlu,
            descriere: anuntCurent.descriere,
            specie: anuntCurent.specie,
            rasa: anuntCurent.rasa,
            gen: anuntCurent.gen,
            varsta: anuntCurent.varsta,
            varstaMin: anuntCurent.varstaMin,
            varstaMax: anuntCurent.varstaMax,
            utilizatorId: anuntCurent.utilizatorId,
            listaImagini: anuntCurent.listaImagini,
            stare: newStare,
            createdAt: anuntCurent.createdAt,
            updatedAt: new Date().toISOString(),
            locatieId: anuntCurent.locatieId
        };

        api.put(`/anunturi/${id}`, dto)
            .then(() => {
                setAnunturi(prev =>
                    prev.map(a => (a.id === id ? { ...a, stare: newStare } : a))
                );
            })
            .catch(err => console.error(err));
    };



    return (
        <div className="table-container">
            {/* Cautare + Filtru + Sort */}
            <div style={{ marginBottom: "12px", display: "flex", gap: "12px", flexWrap: "wrap" }}>
                <input
                    type="text"
                    placeholder="Cauta dupa ID, nume sau specie..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    style={{ padding: "6px", flex: "1 1 200px" }}
                />

                <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} style={{ padding: "6px" }}>
                    <option value="TOATE">Toate starile</option>
                    <option value="NEVERIFICAT">Neverificat</option>
                    <option value="ACTIV">Activ</option>
                    <option value="INACTIV">Inactiv</option>
                </select>

                <select value={sortType} onChange={(e) => setSortType(e.target.value)} style={{ padding: "6px" }}>
                    <option value="id_desc">Cele mai recente</option>
                    <option value="titlu_asc">Titlu A → Z</option>
                    <option value="titlu_desc">Titlu Z → A</option>
                </select>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nume</th>
                    <th>Specie</th>
                    <th>Varsta</th>
                    <th>Id Utilizator</th>
                    <th>ID Locatie</th>
                    <th>Stare</th>
                </tr>
                </thead>
                <tbody>
                {anunturiFiltrate.map((a)=> (
                    <tr key={a.id} onClick={() => handleRowClick(a.id)} style={{ cursor: "pointer" }}>
                        <td>{a.id}</td>
                        <td>{a.titlu}</td>
                        <td>{a.specie}</td>
                        <td>{a.varsta}</td>
                        <td>{a.utilizatorId}</td>
                        <td>{a.locatieId}</td>
                        <td>
                            <select
                                value={a.stare}
                                onClick={(e) => e.stopPropagation()}
                                onChange={(e) => handleStareChange(a.id, e.target.value)}
                                style={{ padding: "4px" }}
                            >
                                <option value="NEVERIFICAT">Neverificat</option>
                                <option value="ACTIV">Activ</option>
                                <option value="INACTIV">Inactiv</option>
                            </select>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );

}
